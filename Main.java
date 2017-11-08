import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		String slice = Parser.readFile("projectSSoft/slice2.1.txt");
		String a[] = Parser.createTree(slice).printTree2().split("\n");
		System.out.println(a[0]);
		//Parser.processPattern(Parser.readFile("src/pattern1.txt"));
	}
}
