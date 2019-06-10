package node;

import tools.LinkDTO;
import tools.LnxParser;
import tools.NodeDTO;

import java.net.SocketException;
import java.util.ArrayList;

public class Node {
	private String ip;
	private ArrayList<Interface> interfaces;
	private ArrayList<String> neighbors;

	public Node(String fileName) throws SocketException {
		NodeDTO nodeDTO = LnxParser.parse(fileName);
		this.ip = nodeDTO.getIp();
		int port = nodeDTO.getPort();
		this.interfaces = new ArrayList<>();
		int id = 1;
		for (LinkDTO linkDTO: nodeDTO.getLinkDTOS()){
			// TODO: How find neighbours names?
			this.interfaces.add(new Interface(id++, linkDTO.getSrcVip(), port, linkDTO.getDst().getIp(),
					linkDTO.getDstVip(), linkDTO.getDst().getPort()));
		}
	}

}
