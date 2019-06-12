package ip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

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

    public byte[] getBytes(){
        ArrayList<Byte> bytes = new ArrayList<>();
        for (byte b: srcAddress.getBytes())
            bytes.add(b);
        for (byte b: srcAddress.getBytes())
            bytes.add(b);
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
}