public class Main {

	public static void main(String[] args) {
		String teste = "root {rchild1} filler{ rchild2{rchildchild}";
		Node root = new Node(null);
		Node current = root;
		Node parent = root;
		int ii =0;
		int iii =0;
		char c = 'a';
		while(iii < teste.length()-1) {
			if (c == '{') {
				current = new Node(current);
				current.setKind(Integer.toString(ii));
				parent = current.get_parent();
				parent.addChild(current);
				ii++;
			}
			if (c == '}') {
				current = current.get_parent();
				parent = parent.get_parent();
			}
		c = teste.charAt(iii);
		iii++;
		}
		
		root.printTree();
	}

}
