import node.Node;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Node node;
		try {
			node = new Node("A.lnx");
			System.out.println();
			node.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
