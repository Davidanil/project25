package com.ist.ss.php_vuln_finder.php_vuln_finder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.ist.ss.php_vuln_finder.php_vuln_finder.nodes.*;

public class App {
	private static List<Pattern> patterns;

	private static boolean vulnerable;
	private static List<String> safeVars = new ArrayList<String>();
	private static List<String> sanitizationVars = new ArrayList<String>();
	private static List<String> sinkVars = new ArrayList<String>();

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

	public static List<String> getSanitizationVars() {
		return sanitizationVars;
	}

	public static void setSanitizationVars(List<String> sanitizationVars) {
		App.sanitizationVars = sanitizationVars;
	}

	public static List<String> getSinkVars() {
		return sinkVars;
	}

	public static void setSinkVars(List<String> sinkVars) {
		App.sinkVars = sinkVars;
	}

	public static void main(String[] args) {
		try {
			// Read and print to console the pattern file
			patterns = Pattern.processPatternFile();
			// Pattern.printPatterns(patterns);

			JsonReader reader = new JsonReader(new FileReader("slice1.1.txt"));

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(reader); // Root Element

			if (element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();
				String kind = root.get("kind").getAsString();

				ProgramNode program = new ProgramNode();
				program.setKind(kind);

				createTreeNode(program, root.getAsJsonArray("children"));

				print(getSafeVars().size() + "");

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
				String value = obj.get("value").getAsString();
				child = new StringNode(parent, value);
				break;
			case "variable":
				child = new VariableNode(parent);
				String nameVariable = obj.get("name").getAsString();
				((VariableNode) child).setName(nameVariable);

				if (isSafeVariable(nameVariable))
					safeVars.add(nameVariable);
				else{
					//check if variable was considered safe
					String vulnVarName = findVariableName(child);
					safeVars.remove(vulnVarName);
				}

				// print("nome variavel:" + findVariableName(child));

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
				if (varName.contains(entryPoint.substring(1)))
					return false;

		return true;
	}

	public static String findVariableName(Node node) {
		if (!(node instanceof AssignNode) && node.getParent() != null)
			return findVariableName(node.getParent());
		else {
			return ((VariableNode) ((AssignNode) node).getLeft()).getName();
		}

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
