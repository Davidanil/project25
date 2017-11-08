import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		String teste = Parser.readFile("projectSSoft/slice2.txt");
		Parser.createTree(teste).printTree();
		//Parser.processPattern(Parser.readFile("src/pattern1.txt"));
	}
}
