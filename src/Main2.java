import node.Node;

import java.io.IOException;

public class Main2 {
    public static void main(String[] args) {
        Node node;
        try {
            node = new Node("B.lnx");
            node.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
