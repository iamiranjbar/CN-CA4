package routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DV {

	private HashMap<String, DistanceVectorRow> table;
	
	public DV() {
	    table = new HashMap<>();
	}
	
	private void fillTable(byte[] array) {
		String temp = new String(array);
		String[] splited = temp.split("\\s+");
		for(String part : splited) {
			String to = "";
			int j = 0;
			byte[] current = part.getBytes();
			int fromIndex = 0;
			for (byte item : current) {
				if (item == '\0') {
					fromIndex++;
					break;
				}
				to += (char)item;
				fromIndex++;
			}
			byte[] row = new byte[current.length-fromIndex];
			for(int i = fromIndex; i < current.length; i++) {
				row[j++] = current[i];
			}
			table.put(to, new DistanceVectorRow(row));
		}
	}
	
	public DV(byte[] array, String name) {
		table = new HashMap<>();
		fillTable(array);
	}
	
	// TODO: this method helps to fill table from lnx file.
	public void update(int from, String to, int cost) {
		DistanceVectorRow distanceVectorRow = table.get(to);
		if (distanceVectorRow == null) {
			distanceVectorRow = new DistanceVectorRow();
		}
		distanceVectorRow.update(from, cost);
		table.put(to, distanceVectorRow);
	}

	public void update(DV newDV, int interfaceId, String interfaceIp) {
		this.print();
		newDV.print();

		int costToNew = getCostTo(interfaceIp);
		for(String item : table.keySet()) {
			if (getCostTo(item) > (costToNew + newDV.getCostTo(item))) {
				update(interfaceId, item, costToNew + newDV.getCostTo(item));
			}
		}
	}

	private int getCostTo(String item) {
		return table.get(item).getCost();
	}

	public byte[] getByteArray() {
		ArrayList<Byte> result = new ArrayList<>();
		for (String key : table.keySet()) {
			for (Byte item : key.getBytes()) {
				result.add(item);
			}
			result.add((byte) '\0');
			result.addAll(Arrays.asList(table.get(key).getByteArray()));
			result.add((byte) ' ');
		}
        byte[] res = new byte[result.size()];
        for(int i = 0; i < result.size(); i++) {
            res[i] = result.get(i).byteValue();
        }
		return res;
	}

	public void print() {
		for (String to : table.keySet()) {
			System.out.print(to + " ");
			table.get(to).print();
		}
		System.out.println();
	}

}
