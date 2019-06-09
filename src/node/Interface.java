package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Interface {
	
	private int id;
	private int port;
	private int receiverPort;
	private String ip;
	private String receiverIp;
	private DatagramSocket datagramSocket;
	
	public Interface(int id, String ip, String receiverIp) throws SocketException {
		this.id = id;
		this.ip = ip;
		this.receiverIp = receiverIp;
		datagramSocket = new DatagramSocket(this.port);
	}
	
	public int getId() {
		return id;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getReceiverIp() {
		return receiverIp;
	}
	
	public void send(String packet) throws UnknownHostException, IOException {
		datagramSocket.send(new DatagramPacket(packet.getBytes(), packet.getBytes().length,
				InetAddress.getByName(this.receiverIp), this.receiverPort));
	}
	
	public String receive() throws IOException {
		byte[] data = new byte[64000];
    	DatagramPacket p = new DatagramPacket(data, data.length);
		datagramSocket.receive(p);
		return new String(p.getData());
	}
}
