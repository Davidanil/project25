package com.ist.ss.php_vuln_finder.php_vuln_finder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.AssignNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.BinNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.CallNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.EchoNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.EncapsedNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.IdentifierNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.Node;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.NumberNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.OffsetLookupNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.StringNode;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.VariableNode;

public class Analyser {
	private static List<Pattern> patterns;

	private static boolean vulnerable = false;
	private static List<String> safeVars = new ArrayList<String>();
	private static Map<String, String> sanitizationVars = new HashMap<String, String>();
	private static List<String> sinkFunctionNames = new ArrayList<String>();

	public Analyser() {
	}

	// [BEGIN] GETTERS AND SETTERS --------------------
	public static List<Pattern> getPatterns() {
		return patterns;
	}

	public static void setPatterns(List<Pattern> patterns) {
		Analyser.patterns = patterns;
	}

	public static boolean isVulnerable() {
		return vulnerable;
	}

	public static void setVulnerable(boolean vulnerable) {
		Analyser.vulnerable = vulnerable;
	}

	public static List<String> getSafeVars() {
		return safeVars;
	}

	public static void setSafeVars(List<String> safeVars) {
		Analyser.safeVars = safeVars;
	}

	public static Map<String, String> getSanitizationVars() {
		return sanitizationVars;
	}

	public static void setSanitizationVars(Map<String, String> sanitizationVars) {
		Analyser.sanitizationVars = sanitizationVars;
	}

	public static List<String> getSinkFunctionNames() {
		return sinkFunctionNames;
	}

	public static void setSinkFunctionNames(List<String> sinkFunctionNames) {
		Analyser.sinkFunctionNames = sinkFunctionNames;
	}
	// [END] GETTERS AND SETTERS --------------------

	
	// Recursive function that receives a node, representing the parent, and its children in the form of
	// a JsonArray. The function iterates over this array to create the child nodes of the parent.
	// If child also has children, createTreeNode is called recursively.
	public static void createTreeNode(Node parent, JsonArray childrenArray) {
		int countElements = 0;

		for (JsonElement element : childrenArray) {
			countElements++; // Allows to know how many elements were already seen
			JsonObject obj = element.getAsJsonObject();
			String kind = obj.get("kind").getAsString();

			Node child = null;
			JsonArray childChildrenArray = new JsonArray();

			// TODO: Replace this bigass switch with a OOP solution using:
			// https://goo.gl/Pk11ut
			// Create node according to its kind
			switch (kind) {
			case "assign":
				child = new AssignNode(parent);
				childChildrenArray.add(obj.getAsJsonObject("left"));
				childChildrenArray.add(obj.getAsJsonObject("right"));
				break;
			case "bin":
				child = new BinNode(parent);
				childChildrenArray.add(obj.getAsJsonObject("left"));
				childChildrenArray.add(obj.getAsJsonObject("right"));
				break;
			case "call":
				child = new CallNode(parent);
				childChildrenArray.add(obj.getAsJsonObject("what"));
				for (JsonElement arg : obj.getAsJsonArray("arguments"))
					childChildrenArray.add(arg.getAsJsonObject());
				break;
			case "echo":
				child = new EchoNode(parent);
				childChildrenArray = obj.getAsJsonArray("arguments");
				break;
			case "identifier":
				child = new IdentifierNode(parent);
				String nameIdentifier = obj.get("name").getAsString();
				((IdentifierNode) child).setName(nameIdentifier);
				break;
			case "offsetlookup":
				child = new OffsetLookupNode(parent);
				childChildrenArray.add(obj.getAsJsonObject("what"));
				childChildrenArray.add(obj.getAsJsonObject("offset"));
				break;
			case "string":
				String valueString = obj.get("value").getAsString();
				child = new StringNode(parent, valueString);
				break;
			case "number":
				String valueNumber = obj.get("value").getAsString();
				child = new NumberNode(parent, valueNumber);
				break;
			case "encapsed":
				child = new EncapsedNode(parent);
				childChildrenArray = obj.getAsJsonArray("value");
				break;
			case "variable":
				child = new VariableNode(parent);
				String nameVariable = obj.get("name").getAsString();
				((VariableNode) child).setName(nameVariable);
				break;
			}

			// Update child kind and, if it has children, recall this function
			if (child != null) {
				child.setKind(kind);

				// Set children nodes of parent equal to the children nodes created in the last switch
				if (parent instanceof OffsetLookupNode) {
					if (countElements == 1)
						((OffsetLookupNode) parent).setWhat(child);
					else if (countElements == 2)
						((OffsetLookupNode) parent).setOffset(child);
				} else if (parent instanceof AssignNode) {
					if (countElements == 1)
						((AssignNode) parent).setLeft(child);
					else if (countElements == 2)
						((AssignNode) parent).setRight(child);
				} else if (parent instanceof BinNode) {
					if (countElements == 1)
						((BinNode) parent).setLeft(child);
					else if (countElements == 2)
						((BinNode) parent).setRight(child);
				} else if (parent instanceof CallNode) {
					if (countElements == 1)
						((CallNode) parent).setWhat(child);
					else
						parent.addChild(child);
				} else
					parent.addChild(child);

				// Identify safe variables and fill our lists with useful info
				if (child.getKind().equals("variable")) {
					String childName = ((VariableNode) child).getName();
					String leftVarName = findVariableName(parent);

					// if parent is an assign node and has child has a left children
					// we assume child is safe and its added to safeVars (eg. a = ?  => a its safe)
					if ((parent instanceof AssignNode) && ((AssignNode) parent).getLeft().equals(child))
						safeVars.add(childName);
					// if parent isnt an Assign Node, and knowing that child is a variable
					// we know that its a potential vulnerable variable
					else if (!(parent instanceof AssignNode))
						fillSaniVarsAndSinkFuncNames(leftVarName, childName, parent, "");
				}

				// If childChildrenArray is null means child doesnt have
				// children. if its not null we do a recursive call to find itschildren
				if (childChildrenArray.size() > 0)
					createTreeNode(child, childChildrenArray);
			}
		}
	}

	
	// leftVarName and rightVarName are constant in every recursive call.
	// node is the node we are analysing for the moment. Recursive call always uses node.getParent() to climb
	// saniFunc indicates if or first sanitizationFunction was already seen in the tree climbing
	
	// Function searches if rightVarName belongs to a CallNode, whether a Sanitization Function or a Sensitive Sink.
	// Its climbs the tree until it finds a Sensitive sink or the assign node with the same leftVarName
	public static void fillSaniVarsAndSinkFuncNames(String leftVarName, String rightVarName, Node node, String saniFunc) {

		if (node instanceof CallNode) {
			String funcName = ((IdentifierNode) ((CallNode) node).getWhat()).getName();

			if (isSanitizationFunction(funcName)) {
				// if a sanitization call is used, that means leftVarName is a composed variable
				// and should be removed from safeVars list
				safeVars.remove(leftVarName);

				// update saniFunc to first sanitization Function found
				String auxSaniFunction = saniFunc;
				if (saniFunc.length() == 0)
					auxSaniFunction = funcName;

				// continue climbing tree
				if (node.getParent() != null)
					fillSaniVarsAndSinkFuncNames(leftVarName, rightVarName, node.getParent(), auxSaniFunction);
			}
			else if (isSensitiveSink(funcName)) {
				// if a sanitization call is used, that means leftVarName is a composed variable
				// and should be removed from safeVars list
				safeVars.remove(leftVarName);

				// if rightVarName is the left argument of the call node (eg. mysql_query('evil','options=safe'))
				// and its considered a vulnerable variable
				if (isVarLeftSide(((CallNode) node).getArguments().get(0), rightVarName)
						&& !safeVars.contains(rightVarName) && !sanitizationVars.containsKey(rightVarName))
					vulnerable = true;
				else if (sanitizationVars.containsKey(rightVarName)) {
					// get function that helped clean rightVarName
					sinkFunctionNames.add(sanitizationVars.get(rightVarName));
				}
				return;
			}
		} 
		else if (node instanceof AssignNode) {
			// if we found a sanitization function and leftVarName was vulnerable
			if ((saniFunc.length() != 0) && !safeVars.contains(leftVarName))
				sanitizationVars.put(leftVarName, saniFunc);
			// if no sanitization function was found  and 
			// rightVarName is an unsafe variable OR it isnt in the safe Lists
			else if ((saniFunc.length() == 0) && (!isSafeVariable(rightVarName) || !safeVars.contains(rightVarName)
					|| !sanitizationVars.containsKey(rightVarName)))
				safeVars.remove(leftVarName);
			return;
		} 
		//continue climbling
		else if (node.getParent() != null)
			fillSaniVarsAndSinkFuncNames(leftVarName, rightVarName, node.getParent(), saniFunc);

	}

	public static boolean isVarLeftSide(Node node, String value) {
		if (node instanceof VariableNode) {
			String varName = ((VariableNode) node).getName();
			if (varName.equals(value))
				return true;
		}

		// if(node.getChildren().isEmpty())
		// return false;
		//
		// for(Node n : node.getChildren())
		// return isVarLeftSide(n, value);

		return false;
	}

	public static boolean isSafeVariable(String varName) {
		for (Pattern pattern : getPatterns())
			for (String entryPoint : pattern.getEntryPoints())
				if (varName.equals(entryPoint.substring(1)))
					return false;

		return true;
	}

	public static boolean isSanitizationFunction(String funcName) {
		for (Pattern pattern : getPatterns())
			for (String sanitizationFunc : pattern.getSanitizationFunctions())
				if (funcName.equals(sanitizationFunc))
					return true;
		return false;
	}

	public static boolean isSensitiveSink(String sinkName) {
		for (Pattern pattern : getPatterns())
			for (String sensitiveSink : pattern.getSensitiveSinks())
				if (sinkName.equals(sensitiveSink))
					return true;
		return false;
	}

	public static String findVariableName(Node node) {
		if (!(node instanceof AssignNode) && node.getParent() != null)
			return findVariableName(node.getParent());
		else if (node instanceof AssignNode)
			return ((VariableNode) ((AssignNode) node).getLeft()).getName();
		else
			return null;

	}

	public static void printFinalState(){
		System.out.println("Vulnerable: " + vulnerable);

		System.out.println("==========================");
		System.out.println("Safe Variables: " + safeVars.size());
		for (String safeVar : safeVars)
			System.out.println("*" + safeVar);

		System.out.println("==========================");
		System.out.println("Sanitization: " + sanitizationVars.size());
		System.out.println(sanitizationVars.toString());

		System.out.println("==========================");
		System.out.println("Sensitive Sink: " + sinkFunctionNames.size());
		for (String sinkFunctionName : sinkFunctionNames)
			System.out.println("*" + sinkFunctionName);
		// printTreeNode(program);
	}
}
