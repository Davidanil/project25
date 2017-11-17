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
	// args[0] should be the name of the file we want to analyse
	public static void main(String[] args) {
		try {
			// Read and print to console the pattern file
			List<Pattern> patterns = Pattern.processPatternFile();
			Analyser.setPatterns(patterns);
			//Pattern.printPatterns(Analyser.getPatterns());
			
			//TODO: use args[0]
			JsonReader reader = new JsonReader(new FileReader("slice1.1.json"));

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(reader); // Root Element

			if (element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();
				String kind = root.get("kind").getAsString();

				ProgramNode program = new ProgramNode();
				program.setKind(kind);

				//Create tree
				Analyser.createTreeNode(program, root.getAsJsonArray("children"));
				
				//Print created tree
				printTreeNode(program);

				//Print final state after executing the algorithm
				Analyser.printFinalState();

			}

		} catch (FileNotFoundException e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}

	public static void printTreeNode(Node node) {
		for (int i = 0; i < node.getLevel(); i++)
			System.out.print("\t");

		System.out.println(node.getKind());
		for (Node n : node.getChildren()) {
			printTreeNode(n);
		}
	}
}
