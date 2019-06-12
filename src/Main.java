import node.Node;
import routing.DV;
import tools.LnxParser;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException {
//		Interface Minterface = new Interface(0, "192.168.0.1", "192.168.0.2", 5001, 5000);
//        DV dv = new DV("192.168.0.2");
//        dv.update("192.168.0.2", "192.168.0.2", 4);
//        dv.update("192.168.0.2", "192.168.0.3", 1);
//		DV dv2 = new DV("192.168.0.3");
//		dv2.update("192.168.0.3", "192.168.0.2", 1);
//		dv2.update("192.168.0.3", "192.168.0.3", 0);
//		dv.update(dv2);
//		dv.print();
//		Minterface.send(new String(dv.getByteArray()));
//		System.out.println(LnxParser.parse("long1.lnx"));
		Node node = new Node("long1.lnx");
		node.notifyNeighbors();
	}
}
