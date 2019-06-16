package forwarding;

import java.util.HashMap;

import node.Interface;
import routing.DV;

public class ForwardingTable {
	
	private HashMap<String, Integer> table;
	
	public ForwardingTable() {
		table = new HashMap<>();
	}
	
	public void update(String vIp, int id) {
		table.put(vIp, id);
	}
	
	public void update(DV dv) {
		for (String ip : dv.getDestinations()) {
			table.put(ip, dv.getInterfaceOfIp(ip));
		}
	}
	
	public int getId(String vIp) {
		return table.get(vIp);
	}

	public void print() {
		System.out.println("id\tDestination Address");
		for (String address : table.keySet())
			System.out.println(table.get(address) + "\t" + address);
		System.out.println("****************************************************************");	
	}

}
