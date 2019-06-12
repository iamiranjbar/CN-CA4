package forwarding;

import java.util.HashMap;

import routing.DV;

public class ForwardingTable {
	
	private HashMap<String, Integer> table;
	
	public ForwardingTable() {
		table = new HashMap<>();
	}
	
	public void update(String vIp, int port) {
		table.put(vIp, port);
	}
	
	public void update(DV dv) {
		
	}
	
	public int getPort(String vIp) {
		return table.get(vIp);
	}

}
