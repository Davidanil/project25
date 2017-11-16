import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		//Parser.processPattern(Parser.readFile("src/pattern1.txt"));
		VulnerabilityList  v = new VulnerabilityList();
		String slice = Parser.readFile("slice2.1.txt");
		
		String potvuln = Parser.createTree(slice).printTree2(v);
		String truevuln = Parser.createTree(slice).printTree(v);
		System.out.println("Potenciais: \n" + potvuln + "\n True: \n" + truevuln);

		crisscross(potvuln.split("\n"), truevuln.split("\n"));

		for(String s : v.getEntries())
			System.out.println(s);
		System.out.println("------");
		for(String s : v.getSinks())
			System.out.println(s);
		System.out.println("------");
		for(String s : v.getSafe())
			System.out.println(s);
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
}
