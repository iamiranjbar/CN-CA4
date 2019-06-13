package routing;

import tools.Serializer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class DistanceVectorRow {
	
	private int cost;
	private int interfaceId;
	
	private void fillCosts(byte[] array) {
		byte[] rowId = {array[0], array[1], array[2], array[3]};
		byte[] rowCost = {array[4], array[5], array[6], array[7]};
		ByteBuffer bf = ByteBuffer.wrap(rowId);
		ByteBuffer bf2 = ByteBuffer.wrap(rowCost);
		interfaceId = bf.getInt();
		cost = bf2.getInt();
	}
	
	public DistanceVectorRow(byte[] array) {
		fillCosts(array);
	}

	public DistanceVectorRow() {

	}

	public void print() {
		System.out.print(interfaceId + " " + cost + " ");
	}
	
	public void update(int from, int newCost) {
//		if (cost > newCost) {
			this.cost = newCost;
			this.interfaceId = from;
//		}
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public int getInterfaceId() {
		return interfaceId;
	}
	
	public Byte[] getByteArray() {
		ArrayList<Byte> result = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(interfaceId);
		for(byte item :  buffer.array()) {
			result.add(item);
		}
		ByteBuffer buffer2 = ByteBuffer.allocate(4);
		buffer2.putInt(cost);
		for(byte item :  buffer2.array()) {
			result.add(item);
		}
		Byte[] res = new Byte[result.size()];
		for(int i = 0; i < result.size(); i++) {
			res[i] = result.get(i).byteValue();
		}
		return res;
	}
}
