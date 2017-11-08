import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		//Parser.processPattern(Parser.readFile("src/pattern1.txt"));
		String slice = Parser.readFile("projectSSoft/slice2.1.txt");
		String potvuln = Parser.createTree(slice).printTree2();
		String truevuln = Parser.createTree(slice).printTree();
		System.out.println("Potenciais: \n" + potvuln + "\n True: \n" + truevuln);
		crisscross(potvuln.split("\n"), truevuln.split("\n"));
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
