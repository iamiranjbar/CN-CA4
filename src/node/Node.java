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
	static Semaphore semaphore = new Semaphore(1);

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
//				System.out.println("notified!");
			}
			this.recieve();
//			showDV();
//			System.out.println("*****************");
//			this.distanceVector.print();
//			System.out.println("*****************");
		}
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
		}
	}

	public void notifyNeighbors() throws IOException {
		byte[] updatedDV = this.distanceVector.getByteArray();
		for (Interface face:interfaces) {
			IPDatagaram ipDatagaram = new IPDatagaram(face.getvIp(), face.getReceiverVIp(), updatedDV, updatedDV.length,
					200, 150);
			face.send(ipDatagaram.getBytes());
		}
	}

	public void recieve(){
		for (Interface face: interfaces){
			try {
				byte[] recieved = face.receive();
//				System.out.println("recived!");
				IPDatagaram ipDatagaram = new IPDatagaram(recieved);
				//TODO: Call handler for protocol null -> Update. Print, TTL--
				handel(ipDatagaram, face);
//				showDV();
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	private void handel(IPDatagaram ipDatagaram, Interface face) throws InterruptedException {
		semaphore.acquire();
		try {
			DV newDV = new DV(ipDatagaram.getData());
			this.distanceVector.update(newDV, face.getId(), face.getReceiverVIp());
			forwardingTable.update(distanceVector);
		} finally {
			semaphore.release();
		}
	}

	public void showInterfacesInfo(){
		System.out.println("id\tSource Address\tDestination Address");
		for (Interface face:interfaces)
			System.out.println(face);
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
