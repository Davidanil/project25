package com.ist.ss.php_vuln_finder.php_vuln_finder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pattern {
	private String vulnName;
	private List<String> entryPoints = new ArrayList<String>();
	private List<String> sanitizationFunctions = new ArrayList<String>();
	private List<String> sensitiveSinks = new ArrayList<String>();

	private static final String PATH = "pattern.txt";

	public String getVulnName() {
		return vulnName;
	}

	public void setVulnName(String vulnName) {
		this.vulnName = vulnName;
	}

	public List<String> getEntryPoints() {
		return entryPoints;
	}

	public void setEntryPoints(List<String> entryPoints) {
		this.entryPoints = entryPoints;
	}

	public List<String> getSanitizationFunctions() {
		return sanitizationFunctions;
	}

	public void setSanitizationFunctions(List<String> sanitizationFunctions) {
		this.sanitizationFunctions = sanitizationFunctions;
	}

	public List<String> getSensitiveSinks() {
		return sensitiveSinks;
	}

	public void setSensitiveSinks(List<String> sensitiveSinks) {
		this.sensitiveSinks = sensitiveSinks;
	}

	/**
	 * @return - List of patterns read from the PATH file, with attributes 
	 * all set
	 */
	public static List<Pattern> processPatternFile() {
		List<Pattern> patterns = new ArrayList<Pattern>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(PATH));
			String line;

			while ((line = br.readLine()) != null) {
				// set Vulnerbility Name
				Pattern pattern = new Pattern();
				pattern.setVulnName(line.trim());

				// set Entry Points
				line = br.readLine().trim();
				for (String entryPoint : line.split(","))
					pattern.getEntryPoints().add(entryPoint);

				// set Sanitization Functions
				line = br.readLine().trim();
				for (String sanitizationFunction : line.split(","))
					pattern.getSanitizationFunctions().add(sanitizationFunction);

				// set Sensitive Sinks
				line = br.readLine().trim();
				for (String sensitiveSinks : line.split(","))
					pattern.getSensitiveSinks().add(sensitiveSinks);

				patterns.add(pattern);

				// read \n separating patterns
				line = br.readLine();
			}
			
			br.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return patterns;
	}

	/**
	 *  Auxiliar function that iterates and prints every pattern in the list 
	 *  returned by processPatternFile
	 */
	public static void printPatterns() {
		List<Pattern> patterns = processPatternFile();
		
		for(Pattern pattern : patterns){
			System.out.println(pattern.vulnName);
			System.out.println("--");
			for(String entry : pattern.entryPoints)
				System.out.println(entry);
			System.out.println("--");
			for(String sanitization : pattern.sanitizationFunctions)
				System.out.println(sanitization);
			System.out.println("--");
			for(String sensitive : pattern.sensitiveSinks)
				System.out.println(sensitive);
			System.out.println("----------");
		}
			
	}
}
