package ip;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class IPDatagaram{
    private String srcAddress;
    private String dstAddress;
    private byte[] data;
    private int totalLength;
    private int protocolNum;
    private int TTL;

    public IPDatagaram(String srcAddress, String dstAddress, byte[] data, int totalLength, int protocolNum, int TTL) {
        this.srcAddress = srcAddress;
        this.dstAddress = dstAddress;
        this.data = data;
        this.totalLength = totalLength;
        this.protocolNum = protocolNum;
        this.TTL = TTL;
    }

    public IPDatagaram(byte[] serializedByteArray) throws Exception {
    	if (serializedByteArray.length < 34 )
    		throw new Exception("packet length is too short.");
        Integer start = 0;
        ArrayList<Object> temp = findNextString(start, serializedByteArray);
        this.srcAddress = (String) temp.get(1);
        start = (Integer) temp.get(0);
        temp = findNextString(start, serializedByteArray);
        this.dstAddress = (String) temp.get(1);
        start = (Integer) temp.get(0); 
        temp = findNextInt(start, serializedByteArray);
        this.totalLength = (int) temp.get(1);
        start = (Integer) temp.get(0);
        temp = findNextInt(start, serializedByteArray);
        this.protocolNum = (int) temp.get(1);
        start = (Integer) temp.get(0);
        temp = findNextInt(start, serializedByteArray);
        this.TTL = (int) temp.get(1);
        start = (Integer) temp.get(0);
        this.data = new byte[serializedByteArray.length - start];
        this.fillData(start, serializedByteArray);
    }

    private void fillData(Integer start, byte[] byteArray){
        int j = 0;
        for (int i = start; i < byteArray.length; i++) {
            this.data[j++] = byteArray[i];
        }
    }

    private ArrayList<Object> findNextString(Integer start, byte[] byteArray){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < byteArray.length; i++){
            if (byteArray[i] == '\0') {
                start = i +1;
                ArrayList<Object> res = new ArrayList<>();
                res.add(start);
                res.add(stringBuilder.toString());
                return res;
            }
            stringBuilder.append((char)byteArray[i]);
        }
        return null;
    }

    private ArrayList<Object> findNextInt(Integer start, byte[] byteArray){
        byte[] intBytes = {byteArray[start++], byteArray[start++], byteArray[start++], byteArray[start++]};
        ByteBuffer bf = ByteBuffer.wrap(intBytes);
        ArrayList<Object> res = new ArrayList<>();
        res.add(start);
        res.add(bf.getInt());
        return res;
    }

    public byte[] getBytes(){
        ArrayList<Byte> bytes = new ArrayList<>();
        for (byte b: srcAddress.getBytes())
            bytes.add(b);
        bytes.add((byte)'\0');
        for (byte b: dstAddress.getBytes())
            bytes.add(b);
        bytes.add((byte)'\0');
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(this.totalLength);
        for (byte b: byteBuffer.array())
            bytes.add(b);
        byteBuffer.clear();
        byteBuffer.putInt(this.protocolNum);
        for (byte b: byteBuffer.array())
            bytes.add(b);
        byteBuffer.clear();
        byteBuffer.putInt(this.TTL);
        for (byte b: byteBuffer.array())
            bytes.add(b);
        for (byte b: data)
            bytes.add(b);
        byte[] res = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++)
            res[i] = bytes.get(i);
        return res;
    }

    public int getProtocolNum() {
        return protocolNum;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public int getTTL() {
        return TTL;
    }

    public byte[] getData() {
        return data;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public String getSrcAddress() {
        return srcAddress;
    }
}