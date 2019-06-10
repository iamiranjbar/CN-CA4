import java.io.IOException;
import java.net.UnknownHostException;

import node.Interface;
import routing.DV;
import tools.HostDTO;
import tools.LnxParser;

public class Main {

    public static void main(String[] args) throws UnknownHostException, IOException {
//    	DV dv = new DV();
//    	dv.update( "192.168.0.3", "192.168.0.2", 1);
//    	dv.update( "192.168.0.4", "192.168.0.2", 1);
//    	dv.update( "192.168.0.8", "192.168.0.5", 1);
//    	System.out.println(new String(dv.getByteArray()));
//    	System.out.println(new String(new DV(dv.getByteArray()).getByteArray()));
//    	new Interface(0, "127.0.0.1","127.0.0.1", 5000,5001).send(new String(dv.getByteArray()));
		HostDTO hostDTO = LnxParser.parse("long1.lnx");
		System.out.println(hostDTO.toString());
    }
}
