package routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class DV {

	private HashMap<String, DistanceVectorRow> table;
	private boolean isChanged = true;
	static Semaphore semaphore = new Semaphore(1);
	
	public DV() {
	    table = new HashMap<>();
	}
	
	private void fillTable(byte[] array) {
		String temp = new String(array);
		String[] splited = temp.split("\\s+");
		int o = 0;
		for(String part : splited) {
			if(o == (splited.length - 1)) {
				break;
			}
//			System.out.println(">>>>>>>>>>>>>>>>>> " + part);
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
			o++;
		}
	}
	
	public DV(byte[] array) {
		table = new HashMap<>();
		fillTable(array);
	}
	
	public void update(int from, String to, int cost) {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DistanceVectorRow distanceVectorRow = table.get(to);
		if (distanceVectorRow == null) {
			distanceVectorRow = new DistanceVectorRow();
		}
		distanceVectorRow.update(from, cost);
		table.put(to, distanceVectorRow);
		semaphore.release();
	}

	public void update(DV newDV, int interfaceId, String senderIp, String reciverIp) {
//		System.out.println(">>>>>>>>>>>>>>");
//		this.print();
//		System.out.println("llllllllllllllllllllllllllllllllllllll");
//		newDV.print();
//		System.out.println("pppppppppppppppppppppp");
		int costToNew = getCostTo(senderIp);
//		System.out.println(costToNew +" "+ senderIp);
		if (newDV.getCostTo(reciverIp) == 66 || newDV.getCostTo(senderIp) == 66) {
			for(String ip: table.keySet()) {
				if (table.get(ip).getInterfaceId() == interfaceId) {
					update(interfaceId, ip, 66);
				}
			}
			this.isChanged = true;
		}
		for(String item : table.keySet()) {
			try {
//				if (newDV.getCostTo(item) == 66 && table.get(item).getInterfaceId() == interfaceId) {
//					for(String ip: table.keySet()) {
//						if (table.get(ip).getInterfaceId() == interfaceId) {
//							update(interfaceId, ip, 66);
//						}
//					}
//					this.isChanged = true;
//				} else 
				if (getCostTo(item) > (costToNew + newDV.getCostTo(item))) {
					update(interfaceId, item, costToNew + newDV.getCostTo(item));
					this.isChanged = true;
				}
			} catch(Exception e) {
				
			}
		}
		for (String ip : newDV.getDestinations()) {
			if (!table.containsKey(ip)) {
				update(interfaceId, ip, costToNew + newDV.getCostTo(ip));
				this.isChanged = true;
			}
		}
//		this.print();
	}

	public int getCostTo(String item) {
		return table.get(item).getCost();
	}
	
	public int getInterfaceOfIp(String ip) {
		return table.get(ip).getInterfaceId();
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
			System.out.println();
		}
//		System.out.println();
	}

	public void setUnchanged() {
		isChanged = false;
	}

	public boolean isChanged(){
		return isChanged;
	}

	public Set<String> getDestinations() {
		return table.keySet();
	}
}
