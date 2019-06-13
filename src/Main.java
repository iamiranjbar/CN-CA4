import node.Node;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Node node;
		try {
			node = Node.getInstance();
			node.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
