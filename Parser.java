import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

	public static String readFile(String path) throws IOException {
		String teste="";
		BufferedReader br = new BufferedReader(new FileReader(path));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    teste = sb.toString();
		} finally {
		    br.close();
		}
		return teste;
	}
	
	public static ArrayList<String> processPattern(String pattrn){
		ArrayList<String> temp = new ArrayList<String>();
		for (String item : pattrn.split("\n"))
			if(!item.trim().isEmpty() && item != "\n")
				temp.add(item);
//		int i =0;
//		for(i=0; i < temp.size(); i++)
//			System.out.println(temp.get(i));
			
		return temp;
	}
	
	public static Node createTree(String teste) {
		Node root = new Node(null);
		Node current = root;
		Node parent = root;
		int ii =0;
		int iii =0;
		char c = 'a';
		String temp = "";
		boolean kind = false;
		while(iii < teste.length()-1) {
			if (c == '{') {
				current.addText(temp);
				temp = "";
				current = new Node(current);
				current.setKind(Integer.toString(ii));
				parent = current.get_parent();
				parent.addChild(current);
				ii++;
				kind = true;
			}
			
			if (c == ':' && kind == true) {
				temp = "";
				iii = iii+2;
				while(c != '\"') {
					c = teste.charAt(iii);
					temp += c;
					iii++;
				}
				kind = false;
				current.setKind(temp.substring(0, temp.length()-1));
			}
			if (c == '}') {
				current.addText(temp);
				temp = "";
				current = current.get_parent();
				parent = parent.get_parent();
			}
		c = teste.charAt(iii);
		temp += c;
		iii++;
		}
		return root;
	}
	
}
