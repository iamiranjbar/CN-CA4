package node;

import cli.CLI;
import forwarding.ForwardingTable;
import handler.DataHandler;
import handler.RoutingHandler;
import ip.IPDatagaram;
import routing.DV;
import tools.LinkDTO;
import tools.LnxParser;
import tools.NodeDTO;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Node {

	private String name;
	private boolean isRunning;
	private String ip;
	private int port;
	private DatagramSocket datagramSocket;
	private ArrayList<Interface> interfaces;
	private HashMap<Integer, Method> handlers;
	private ForwardingTable forwardingTable;
	private DV distanceVector;
	private int maxCost;

	public Node(String fileName) throws IOException, NoSuchMethodException {
		NodeDTO nodeDTO = LnxParser.parse(fileName);
		this.name = nodeDTO.getName();
		this.isRunning = true;
		this.ip = nodeDTO.getIp();
		this.port = nodeDTO.getPort();
		this.datagramSocket = new DatagramSocket(this.port);
		this.datagramSocket.setSoTimeout(800);
		this.interfaces = new ArrayList<>();
		this.fillInterfaces(nodeDTO);
		this.forwardingTable = new ForwardingTable();
		this.distanceVector = new DV();
		this.fillDV();
		this.notifyNeighbors();
		Thread thread = new Thread(new CLI(this));
		thread.start();
		this.registerHandler(200, RoutingHandler.class.getMethod("run", Node.class, IPDatagaram.class));
		this.registerHandler(0, DataHandler.class.getMethod("run", Node.class, IPDatagaram.class));
	}

	public void quit() {
		this.isRunning = false;
	}

	public boolean isRunning(){
		return isRunning;
	}

	private void updateMaxCost() {
		int temp = 0;
		for( Interface face : interfaces) {
			if (!face.isEnable()) {
				return;
			}
		}
		for(String ip : distanceVector.getDestinations()) {
			if (temp < distanceVector.getCostTo(ip)) {
				temp = distanceVector.getCostTo(ip);
			}
		}
		maxCost = temp;
	}

	public void run() throws IOException {
		Date current = new Date();
		while (true){
			updateMaxCost();
			if ((new Date().getTime()) - current.getTime() > 1000 && this.distanceVector.isChanged()) {
				this.notifyNeighbors();
			}
			this.recieve();
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

	public void sendData(String dstVIp, int protocolNum, String payload) throws IOException { //TODO: Should test with handlers
		//TODO: if not have dstVip should flood?!
		int outInterface = forwardingTable.getId(dstVIp);
		Interface face = interfaces.get(outInterface-1);
		byte[] sendBytes = payload.getBytes();
		IPDatagaram ipDatagaram = new IPDatagaram(face.getvIp(), face.getReceiverVIp(), sendBytes, sendBytes.length,
				protocolNum, 150);
		face.send(ipDatagaram.getBytes());
	}

	public void recieve(){
		for (Interface face: interfaces){
			 if (face.isEnable()){
				try {
					byte[] recieved = face.receive();
					IPDatagaram ipDatagaram = new IPDatagaram(recieved);
					int protocolNum = ipDatagaram.getProtocolNum();
					handlers.get(protocolNum).invoke(null, this, ipDatagaram);
					if (ipDatagaram.getDstAddress() != face.getvIp())
						this.sendData(ipDatagaram.getDstAddress(), protocolNum, Arrays.toString(ipDatagaram.getData()));
//					showDV();
				} catch (Exception e) {
					continue;
				}
			 }
		}
	}
	
	public int findInterfaceId(String ip) {
		for(Interface face : interfaces) {
			if (face.getvIp().equals(ip)) {
				return face.getId();
			}
		}
		return 0;
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

	public int getMaxCost() {
		return this.maxCost;
	}

	public Interface getInterfaceById(int interfaceId) {
		return interfaces.get(interfaceId);
	}

	public void updateRoutingTables (DV newDV, int interfaceId, IPDatagaram ipDatagaram) {
		this.distanceVector.update(newDV, interfaceId, ipDatagaram.getSrcAddress(), ipDatagaram.getDstAddress());
		forwardingTable.update(distanceVector);
	}

	public void registerHandler(int protocolNum, Method method) {
		handlers.put(protocolNum, method);
	}
}