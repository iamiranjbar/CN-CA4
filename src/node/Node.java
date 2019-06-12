package node;

import forwarding.ForwardingTable;
import tools.LinkDTO;
import tools.LnxParser;
import tools.NodeDTO;

import java.net.SocketException;
import java.util.ArrayList;

public class Node {
	private String ip;
	private ArrayList<Interface> interfaces;
	private ForwardingTable forwardingTable;

	public Node(String fileName) throws SocketException {
		NodeDTO nodeDTO = LnxParser.parse(fileName);
		this.ip = nodeDTO.getIp();
		this.interfaces = new ArrayList<>();
		this.fillInterfaces(nodeDTO);
		this.forwardingTable = new ForwardingTable();
		
	}

	private void fillInterfaces(NodeDTO nodeDTO) throws SocketException {
		int id = 1;
		int port = nodeDTO.getPort();
		for (LinkDTO linkDTO: nodeDTO.getLinkDTOS()){
			this.interfaces.add(new Interface(id++, linkDTO.getSrcVip(), port, linkDTO.getDst().getIp(),
					linkDTO.getDstVip(), linkDTO.getDst().getPort()));
		}
	}

}
