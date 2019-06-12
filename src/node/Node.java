package node;

import forwarding.ForwardingTable;
import routing.DV;
import tools.LinkDTO;
import tools.LnxParser;
import tools.NodeDTO;

import java.net.SocketException;
import java.util.ArrayList;

public class Node {
	private String name;
	private String ip;
	private ArrayList<Interface> interfaces;
	private ForwardingTable forwardingTable;
	private DV distanceVector;

	public Node(String fileName) throws SocketException {
		NodeDTO nodeDTO = LnxParser.parse(fileName);
		this.name = nodeDTO.getName();
		this.ip = nodeDTO.getIp();
		this.interfaces = new ArrayList<>();
		this.fillInterfaces(nodeDTO);
		this.forwardingTable = new ForwardingTable();
		this.distanceVector = new DV(this.name);
		this.fillDV();
	}

	private void fillInterfaces(NodeDTO nodeDTO) throws SocketException {
		int id = 1;
		int port = nodeDTO.getPort();
		for (LinkDTO linkDTO: nodeDTO.getLinkDTOS()){
			this.interfaces.add(new Interface(id++, linkDTO.getSrcVip(), port, linkDTO.getDst().getIp(),
					linkDTO.getDstVip(), linkDTO.getDst().getPort()));
		}
	}

	private void fillDV(){
		this.distanceVector.update(this.name, this.name, 0);
		for (Interface face: interfaces){
			this.distanceVector.update(this.name, face.getRecieverVIp(), 1);
		}
	}
}
