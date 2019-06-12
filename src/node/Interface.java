package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Interface {
	
	private int id;
	private String vIp;
	private String receiverIp;
	private String recieverVIp;
	private int receiverPort;
	private DatagramSocket datagramSocket;

	public Interface(int id, String vIp, int port, String receiverIp, String recieverVIp, int receiverPort)
			throws SocketException {
		this.id = id;
		this.vIp = vIp;
		this.receiverIp = receiverIp;
		this.recieverVIp = recieverVIp;
		this.receiverPort = receiverPort;
		datagramSocket = new DatagramSocket(port);
		datagramSocket.setSoTimeout(800);
	}

	public int getId() {
		return id;
	}

	public String getvIp() {
		return vIp;
	}

	public String getReceiverIp() {
		return receiverIp;
	}

	public String getRecieverVIp() {
		return recieverVIp;
	}

	public int getReceiverPort() {
		return receiverPort;
	}

	public void send(String packet) throws UnknownHostException, IOException {
		datagramSocket.send(new DatagramPacket(packet.getBytes(), packet.getBytes().length,
				InetAddress.getByName(this.receiverIp), this.receiverPort));
	}
	
	public String receive() throws IOException {
		byte[] data = new byte[1400];
    	DatagramPacket p = new DatagramPacket(data, data.length);
		datagramSocket.receive(p);
		return new String(p.getData());
	}
}
