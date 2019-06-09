package forwarding;

import java.util.HashMap;

import routing.DV;

public class ForwardingTable {
	
	private HashMap<String, Integer> table;
	
	public ForwardingTable() {
		table = new HashMap<>();
	}
	
	public void update(String ip, int port) {
		table.put(ip, port);
	}
	
	public void update(DV dv) {
		
	}
	
	public int getPort(String ip) {
		return table.get(ip);
	}

}
