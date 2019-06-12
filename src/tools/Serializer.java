package tools;

import java.util.ArrayList;

public class Serializer {

    public static byte[] convertListToArray(ArrayList<Byte> input) {
        byte[] res = new byte[input.size()];
        for(int i = 0; i < input.size(); i++) {
            res[i] = input.get(i).byteValue();
        }
        return res;
    }
}
