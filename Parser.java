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
	
	public static Node createTree(String slice) {
		Node root = new Node(null);
		Node current = root;
		Node parent = root;
		int strIterator =0;
		char c = 'a';
		String temp = "";
		boolean kind = false;
		
		while(strIterator < slice.length()-1) {

			//Check if new block is opened
			if (c == '{') {
				current.addText(temp);
				temp = "";
				current = new Node(current);
				parent = current.get_parent();
				parent.addChild(current);
				kind = true;
			}
			
			//Finding kinds value AKA node name
			if (c == ':' && kind == true) {
				temp = "";
				strIterator = strIterator+2;
				while((c = slice.charAt(strIterator)) != '\"') {
					temp += c;
					strIterator++;
				}
				kind = false;
				current.setKind(temp);
			}

			//Check if new block is closed
			if (c == '}') {
				current.addText(temp);
				temp = "";
				current = current.get_parent();
				parent = parent.get_parent();
			}

			c = slice.charAt(strIterator);
			temp += c;
			strIterator++;
		}
		return root;
	}
	
}
