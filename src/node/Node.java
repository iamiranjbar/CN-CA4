package node;

import cli.CLI;
import forwarding.ForwardingTable;
import ip.IPDatagaram;
import routing.DV;
import tools.LinkDTO;
import tools.LnxParser;
import tools.NodeDTO;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class Node {

	private String name;
	private String ip;
	private int port;
	private DatagramSocket datagramSocket;
	private ArrayList<Interface> interfaces;
	//	private HashMap<Integer, > TODO: Register handler
	private ForwardingTable forwardingTable;
	private DV distanceVector;

	public Node(String fileName) throws IOException {
		NodeDTO nodeDTO = LnxParser.parse(fileName);
		this.name = nodeDTO.getName();
		this.ip = nodeDTO.getIp();
		this.port = nodeDTO.getPort();
		System.out.println(this.port);
		this.datagramSocket = new DatagramSocket(this.port);
		this.datagramSocket.setSoTimeout(800);
		this.interfaces = new ArrayList<>();
//		this.interfaces.add(new Interface(0, "192.168.0.1", 5001, "127.0.0.1","192.168.0.2",  5000));
		this.fillInterfaces(nodeDTO);
		this.forwardingTable = new ForwardingTable();
		this.distanceVector = new DV();
		this.fillDV();
		this.notifyNeighbors();
		Thread thread = new Thread(new CLI(this));
		thread.start();
		System.out.println("12334");
	}

	public void run() throws IOException {
		Date current = new Date();
		while (true){
			if ((new Date().getTime()) - current.getTime() > 1000 && this.distanceVector.isChanged()) {
				this.notifyNeighbors();
//				this.distanceVector.setUnchanged();
//				System.out.println("*****************");
//				this.distanceVector.print();
//				System.out.println("*****************");
			}
			this.recieve();
//			showDV();
//			System.out.println("*****************");
//			this.distanceVector.print();
//			System.out.println("*****************");
		}
	}
	
	public void upInterface(int id) throws IOException {
		interfaces.get(id).enable();
		Interface temp =  interfaces.get(id);
		this.distanceVector.update(temp.getId(), temp.getvIp(), 0);
		this.distanceVector.update(temp.getId(), temp.getReceiverVIp(), 1);
		notifyNeighbors();
	}
	
	public void downInterface(int id) throws IOException {
		Interface temp =  interfaces.get(id);
		this.distanceVector.update(temp.getId(), temp.getReceiverVIp(), 66);
		for (String ip : distanceVector.getDestinations()) {
			if (distanceVector.getInterfaceOfIp(ip) == id && !localAddress(ip)) {
				this.distanceVector.update(temp.getId(), ip, 66);
			}
		}
		System.out.println(">>>>>>>>>>>>>>>");
		this.distanceVector.print();
		System.out.println("<<<<<<<<<<<<<<<");
		notifyNeighbors();
		this.interfaces.get(id).disable();
	}
	
	private boolean localAddress(String ip) {
		for (Interface face : interfaces) {
			if (face.getvIp().equals(ip)) {
				return true;
			}
		}
		return false;
	}

	private void fillInterfaces(NodeDTO nodeDTO) throws SocketException {
		int id = 0;
		for (LinkDTO linkDTO: nodeDTO.getLinkDTOS()){
			this.interfaces.add(new Interface(id++, linkDTO.getSrcVip(), linkDTO.getDst().getIp(),
					linkDTO.getDstVip(), linkDTO.getDst().getPort(),datagramSocket));
		}
	}

	private void fillDV(){
		for (Interface face: interfaces){
			this.distanceVector.update(face.getId(), face.getReceiverVIp(), 1);
			this.distanceVector.update(face.getId(), face.getvIp(), 0);
		}
	}

	public void notifyNeighbors() throws IOException {
		byte[] updatedDV = this.distanceVector.getByteArray();
		for (Interface face:interfaces) {
			if(face.isEnable()) {
				IPDatagaram ipDatagaram = new IPDatagaram(face.getvIp(), face.getReceiverVIp(), updatedDV, updatedDV.length,
						200, 150);
				face.send(ipDatagaram.getBytes());
			}
		}
	}

	public void recieve(){
		for (Interface face: interfaces){
			 if (face.isEnable()){
				try {
					byte[] recieved = face.receive();
	//				System.out.println(new String(recieved));
					IPDatagaram ipDatagaram = new IPDatagaram(recieved);
	//				System.out.println(ipDatagaram.getDstAddress());
	//				System.out.println(ipDatagaram.getSrcAddress());
	//				System.out.println(new String(ipDatagaram.getData()));
					//TODO: Call handler for protocol null -> Update. Print, TTL--
					handel(ipDatagaram, face);
	//				showDV();
				} catch (Exception e) {
					continue;
				}
			 }
		}
	}
	
	private int findInterfaceId(String ip) {
		for(Interface face : interfaces) {
			if (face.getvIp().equals(ip)) {
				return face.getId();
			}
		}
		return 0;
	}
	
	private void handel(IPDatagaram ipDatagaram, Interface face) throws  IOException {
			DV newDV = new DV(ipDatagaram.getData());
			if (newDV.getCostTo(ipDatagaram.getDstAddress()) > 1) {
				downInterface(findInterfaceId(ipDatagaram.getDstAddress()));
				System.out.println("ok " + findInterfaceId(ipDatagaram.getDstAddress()));
			} else if (newDV.getCostTo(ipDatagaram.getDstAddress()) <= 1 && !interfaces.get(findInterfaceId(ipDatagaram.getDstAddress())).isEnable()) {
				upInterface(findInterfaceId(ipDatagaram.getDstAddress()));
				System.out.println("qd " + findInterfaceId(ipDatagaram.getDstAddress()));
			}
//			System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
//			newDV.print();
//			System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
			this.distanceVector.update(newDV, findInterfaceId(ipDatagaram.getDstAddress()), ipDatagaram.getSrcAddress(), ipDatagaram.getDstAddress());
			forwardingTable.update(distanceVector);
	}

	public void showInterfacesInfo(){
		System.out.println("id\tSource Address\tDestination Address");
		for (Interface face:interfaces) {
			if(face.isEnable())
				System.out.println(face);
		}
		System.out.println("****************************************************************");
	}
	
	public void showDV() {
		System.out.println("to\tfrom\tcost");
		distanceVector.print();
		System.out.println("****************************************************************");
	}

	@Override
	public String toString() {
		return "Node{" +
				"name='" + name + '\'' +
				", ip='" + ip +  +
				'}';

	}

	public void showForwardingTable() {
		forwardingTable.print();
	}
}
