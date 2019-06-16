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
	private String receiverVIp;
	private String receiverIp;
	private int receiverPort;
	private boolean isEnable;
	private DatagramSocket datagramSocket;

	public Interface(int id, String vIp, String receiverIp, String recieverVIp, int receiverPort, DatagramSocket datagramSocket) {
		this.id = id;
		this.vIp = vIp;
		this.receiverIp = receiverIp;
		this.receiverVIp = recieverVIp;
		this.receiverPort = receiverPort;
		this.datagramSocket = datagramSocket;
		this.isEnable = true;
	}
	
	public boolean isEnable() {
		return isEnable;
	}
	
	public void disable() {
		this.isEnable = false;
	}
	
	public void enable() {
		this.isEnable = true;
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

	public String getReceiverVIp() {
		return receiverVIp;
	}

	public int getReceiverPort() {
		return receiverPort;
	}

	public void send(byte[] packet) throws UnknownHostException, IOException {
		datagramSocket.send(new DatagramPacket(packet, packet.length,
				InetAddress.getByName(this.receiverIp), this.receiverPort));
	}
	
	public byte[] receive() throws IOException {
		byte[] data = new byte[1400];
    	DatagramPacket p = new DatagramPacket(data, data.length);
		datagramSocket.receive(p);
		return p.getData();
	}

	@Override
	public String toString() {
		return id +"\t" + vIp + "\t\t" + receiverVIp;
	}
}
