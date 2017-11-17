package com.ist.ss.php_vuln_finder.php_vuln_finder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.*;

public class App {
	private static List<Pattern> patterns;

	private static boolean vulnerable = false;

	private static List<String> safeVars = new ArrayList<String>();
	private static Map<String,String> sanitizationVars = new HashMap<String,String>();
	private static List<String> sinkFunctionNames = new ArrayList<String>();

	public static List<Pattern> getPatterns() {
		return patterns;
	}

	public static void setPatterns(List<Pattern> patterns) {
		App.patterns = patterns;
	}

	public static boolean isVulnerable() {
		return vulnerable;
	}

	public static void setVulnerable(boolean vulnerable) {
		App.vulnerable = vulnerable;
	}

	public static List<String> getSafeVars() {
		return safeVars;
	}

	public static void setSafeVars(List<String> safeVars) {
		App.safeVars = safeVars;
	}

	public static Map<String, String> getSanitizationVars() {
		return sanitizationVars;
	}

	public static void setSanitizationVars(Map<String, String> sanitizationVars) {
		App.sanitizationVars = sanitizationVars;
	}

	public static List<String> getSinkFunctionNames() {
		return sinkFunctionNames;
	}

	public static void setSinkFunctionNames(List<String> sinkFunctionNames) {
		App.sinkFunctionNames = sinkFunctionNames;
	}

	public static void main(String[] args) {
		try {
			// Read and print to console the pattern file
			patterns = Pattern.processPatternFile();
			// Pattern.printPatterns(patterns);

			JsonReader reader = new JsonReader(new FileReader("slices/slice7.json"));

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(reader); // Root Element

			if (element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();
				String kind = root.get("kind").getAsString();

				ProgramNode program = new ProgramNode();
				program.setKind(kind);

				createTreeNode(program, root.getAsJsonArray("children"));

				print("Vulnerable: " + vulnerable);
				
				print("==========================");
				print("Safe Vars: " + safeVars.size());
				for(String safeVar : safeVars)
					print("*" + safeVar);
				
				print("==========================");
				print("Sanitization: " + sanitizationVars.size());
				print("*" + sanitizationVars.toString());
				
				print("==========================");
				print("Sensitive Sink: " + sinkFunctionNames.size());
				for(String sinkFunctionName : sinkFunctionNames)
					print("*" + sinkFunctionName);
				// printTreeNode(program);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param parent
	 *            - node parent thats going to be updated
	 * @param childrenArray
	 *            - children json of node parent
	 */
	public static void createTreeNode(Node parent, JsonArray childrenArray) {
		int countElements = 0;

		for (JsonElement element : childrenArray) {
			countElements++; // Allows to know how many elements were already
								// seen
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
				for (JsonElement arg : obj.getAsJsonArray("arguments")) {
					childChildrenArray.add(arg.getAsJsonObject());
				}
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

				// TODO: Use the same solution for the switch, I think...
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

				if (child.getKind().equals("variable")) {
					String childName = ((VariableNode) child).getName();
					String leftVarName = findVariableName(parent);
					
					if ((parent instanceof AssignNode) && ((AssignNode) parent).getLeft().equals(child))
						safeVars.add(childName);
					else if(!(parent instanceof AssignNode))
						fillSaniVarsAndSinkFuncNames(leftVarName, childName, false, parent, null);
					//left Variable of assign node
//					if ((parent instanceof AssignNode) && ((AssignNode) parent).getLeft().equals(child))
//						safeVars.add(childName);
//					//right Variable of assign node
//					else{
//						String leftVarName = findVariableName(parent);
//						
//						if (!(isSafeVariable(childName))) {
//							// check if variable was considered safe
//							safeVars.remove(leftVarName);
//						}
//						fillSaniVarsAndSinkFuncNames(leftVarName, childName, false, parent, null);
//						
//						
//					}
				}

				/*
				 * if childChildrenArray is null means child doesnt have
				 * children. if its not null we do a recursive call to find its
				 * children
				 */
				if (childChildrenArray.size() > 0)
					createTreeNode(child, childChildrenArray);
			}
		}
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
		else if(node instanceof AssignNode)
			return ((VariableNode) ((AssignNode) node).getLeft()).getName();
		else 
			return null;

	}
	
	public static void fillSaniVarsAndSinkFuncNames(
			String leftVarName, String rightVarName, boolean flagFunction, Node node, String saniFunc){
		//print(leftVarName + " : " + node.getKind());
//				//subir ate encontrar assign com o leftvarname igual ou ate encontrar um call duma sink function
		if(node instanceof CallNode){
			String funcName = ((IdentifierNode) ((CallNode) node).getWhat()).getName();
			
			//check if sanitization function
			if(isSanitizationFunction(funcName)){
				//if a sanitization call is used, that means leftVarName is a composed variable
				//and should be removed from safeVars list
				safeVars.remove(leftVarName);
				
				//update saniFunc to first sanitization Function found
				String auxSaniFunction = saniFunc;
				if(!flagFunction)
					auxSaniFunction = funcName;
					
				if(node.getParent() != null)
					fillSaniVarsAndSinkFuncNames(leftVarName, rightVarName, true, node.getParent(), auxSaniFunction);
			}
			
			//check if sink function
			else if(isSensitiveSink(funcName)){
				//if a sanitization call is used, that means leftVarName is a composed variable
				//and should be removed from safeVars list
				safeVars.remove(leftVarName);
				
				//if argument was bad and never cleaned, and 1st argument of call
				if(isVarLeftSide(((CallNode) node).getArguments().get(0), rightVarName) && !safeVars.contains(rightVarName) && !sanitizationVars.containsKey(rightVarName))
						vulnerable = true;
				else if(sanitizationVars.containsKey(rightVarName)){
					//get function that helped clean rightVarName
					sinkFunctionNames.add(sanitizationVars.get(rightVarName));
				}
					
				return;
			}
		}
		else if (node instanceof AssignNode){
			if(saniFunc != null && !safeVars.contains(leftVarName))
				sanitizationVars.put(leftVarName, saniFunc);
//			else if(saniFunc == null && isSafeVariable(rightVarName))
//				safeVars.add(leftVarName);
			else if(saniFunc == null && (!isSafeVariable(rightVarName) || !safeVars.contains(rightVarName) || !sanitizationVars.containsKey(rightVarName)))
				safeVars.remove(leftVarName);
			return;
		}
		else if(node.getParent() != null)
			fillSaniVarsAndSinkFuncNames(leftVarName, rightVarName, flagFunction, node.getParent(), saniFunc);
		
	}
	
	public static boolean isVarLeftSide(Node node, String value){
		if(node instanceof VariableNode){
			String varName = ((VariableNode) node).getName();
			if(varName.equals(value))
				return true;
		}
		
//		if(node.getChildren().isEmpty())
//			return false;
//		
//		for(Node n : node.getChildren())
//			return isVarLeftSide(n, value);
		
		return false;
	}
			

	/**
	 * @param node
	 *            - Node from which you want to build the tree from
	 * @param lvl
	 */
	public static void printTreeNode(Node node) {
		for (int i = 0; i < node.getLevel(); i++)
			System.out.print("\t");

		print(node.getKind());
		for (Node n : node.getChildren()) {
			printTreeNode(n);
		}
	}

	public static void crisscross(String[] a, String[] b) {
		List<String> c = new ArrayList<String>();
		for (String string : a) {
			for (String string2 : b) {
				if (string.equals(string2)) {
					if (!c.contains(string))
						c.add(string);
				}
			}
		}
		System.out.println("Vulnerable: " + !c.isEmpty());
		System.out.print("RESULT: ");
		for (String string : c) {
			System.out.println(string);
		}
	}

	/**
	 * @param childKind
	 *            - kind of child we are searching for
	 * @return Node of first matching child
	 */
	public Node getDirectChildByKind(Node node, String childKind) {
		for (Node child : node.getChildren())
			if (child.getKind().equals(childKind))
				return child;

		return null;
	}

	public static void print(String str) {
		System.out.println(str);
	}
}
