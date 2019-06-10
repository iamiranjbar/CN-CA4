package routing;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class DistanceVectorRow {
	
	private HashMap<String, Integer> costs;
	
	public DistanceVectorRow() {
		costs = new HashMap<>();
	}
	
	private void fillCosts(byte[] array) {
		String temp = new String(array);
		String[] splited = temp.split("@");
		for(String part : splited) {
			String from = "";
			int j = 0;
			byte[] current = part.getBytes();
			int fromIndex = 0;
			for (byte item : current) {
				if (item == '\0') {
					fromIndex++;
					break;
				}
				from += (char)item;
				fromIndex++;
			}
			byte[] row = new byte[current.length-fromIndex];
			for(int i = fromIndex; i < current.length; i++) {
				row[j++] = current[i];
			}
			ByteBuffer bf = ByteBuffer.wrap(row);
			costs.put(from, bf.getInt());
		}
	}
	
	public DistanceVectorRow(byte[] array) {
		costs = new HashMap<>();
		fillCosts(array);
	}

	public void print() {
		for (String from : costs.keySet()){
			System.out.print(from + " " + costs.get(from) + " ");
		}
	}
	
	public void update(String from, int cost) {
		costs.put(from, cost);
	}
	
	public int getCost(String from) {
		return costs.get(from);
	}
	
	public Byte[] getByteArray() {
		ArrayList<Byte> result = new ArrayList<>();
		for(String from : costs.keySet()) {
			for(byte item :  from.getBytes()) {
				result.add(item);
			}
			result.add((byte) '\0');
			ByteBuffer buffer2 = ByteBuffer.allocate(4);
			buffer2.putInt(costs.get(from));
			for(byte item :  buffer2.array()) {
				result.add(item);
			}
			result.add((byte) '@');
		}
		Byte[] res = new Byte[result.size()];
		for(int i = 0; i < result.size(); i++) {
		    res[i] = result.get(i).byteValue();
		}
		return res;
	}
}
