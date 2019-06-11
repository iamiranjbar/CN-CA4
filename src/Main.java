import java.io.IOException;
import java.net.UnknownHostException;

import node.Interface;
import routing.DV;
import tools.HostDTO;
import tools.LnxParser;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Interface Minterface = new Interface(0, "192.168.0.1", "192.168.0.2", 5001, 5000);
		DV dv = new DV("192.168.0.1");
		dv.update("192.168.0.2", "192.168.0.2", 4);
		Minterface.send(new String(dv.getByteArray()));
	}
}
