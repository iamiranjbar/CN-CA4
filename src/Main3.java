import node.Node;

import java.io.IOException;

public class Main3 {
    public static void main(String[] args) {
        Node node;
        try {
            node = new  Node("C.lnx");
            node.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
