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
	public static void main(String[] args) {
		try {
			//Read and print to console the pattern file
			//Pattern.printPatterns();
			
			JsonReader reader = new JsonReader(new FileReader("slice2.txt"));

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(reader); // Root Element

			if (element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();
				String kind = root.get("kind").getAsString();

				ProgramNode program = new ProgramNode();
				program.setKind(kind);
				
				createTreeNode(program, root.getAsJsonArray("children"));

				
				printTreeNode(program);
				
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param parent - node parent thats going to be updated
	 * @param childrenArray - children json of node parent
	 */
	public static void createTreeNode(Node parent, JsonArray childrenArray) {
		int countElements = 0;

		for (JsonElement element : childrenArray) {
			countElements++; //Allows to know how many elements were already seen
			JsonObject obj = element.getAsJsonObject();
			String kind = obj.get("kind").getAsString();

			Node child = null;
			JsonArray childChildrenArray = new JsonArray();
			
			//TODO: Replace this bigass switch with a OOP solution using: https://goo.gl/Pk11ut
			//Create node according to its kind
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
				for(JsonElement arg : obj.getAsJsonArray("arguments")){
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
				break;
			}

			//Update child kind and, if it has children, recall this function
			if (child != null) {
				child.setKind(kind);
				
				//TODO: Use the same solution for the switch, I think...
				if(parent instanceof OffsetLookupNode){
					if(countElements == 1)
						((OffsetLookupNode) parent).setWhat(child);
					else if(countElements == 2)
						((OffsetLookupNode) parent).setOffset(child);
				}
				else if(parent instanceof AssignNode){
					if(countElements == 1)
						((AssignNode) parent).setLeft(child);
					else if(countElements == 2)
						((AssignNode) parent).setRight(child);
				}
				else if(parent instanceof BinNode){
					if(countElements == 1)
						((BinNode) parent).setLeft(child);
					else if(countElements == 2)
						((BinNode) parent).setRight(child);
				}
				else if(parent instanceof CallNode){
					if(countElements == 1)
						((CallNode) parent).setWhat(child);
					else
						parent.addChild(child);
				}
				else
					parent.addChild(child);

				/* if childChildrenArray is null means child doesnt have children.
				 * if its not null we do a recursive call to find its children */
				if (childChildrenArray.size() > 0)
					createTreeNode(child, childChildrenArray);
			}
		}
	}

	/**
	 * @param node - Node from which you want to build the tree from
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
		for(String string : a) {
			for(String string2 : b) {
				if(string.equals(string2)) {
					if(!c.contains(string))
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



	public static void print(String str) {
		System.out.println(str);
	}
}
