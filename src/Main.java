<<<<<<< HEAD
import node.Interface;
=======
import node.Node;
>>>>>>> f234365042e09aaba5be639aed03b52a53225da4
import routing.DV;
import tools.LnxParser;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException {
<<<<<<< HEAD
		Interface Minterface = new Interface(0, "192.168.0.1", 5001, "127.0.0.1", "192.168.0.2", 5000);
        DV dv = new DV();
        dv.update(0, "192.168.0.2", 0);
        dv.update(0, "192.168.0.3", 5);
		DV dv2 = new DV();
		dv2.update(0, "192.168.0.2", 2);
		dv2.update(0, "192.168.0.3", 2);
		dv.update(dv2, 0,"192.168.0.2");
		dv.print();
		Minterface.send(new String(dv.getByteArray()));
=======
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
>>>>>>> f234365042e09aaba5be639aed03b52a53225da4
	}
}
