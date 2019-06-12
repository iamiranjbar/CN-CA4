import node.Interface;
import routing.DV;
import tools.LnxParser;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException {
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
	}
}
