package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LnxParser {
    public static NodeDTO parse(String fileName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            NodeDTO node = readOwnData(reader);
            node.addLinks(readLinks(reader));
            reader.close();
            return node;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NodeDTO readOwnData(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        String[] parts = line.split("\\s+");
        return new NodeDTO(parts[0], Integer.parseInt(parts[1]));
    }

    public static ArrayList<LinkDTO> readLinks(BufferedReader reader) throws IOException {
        ArrayList<LinkDTO> linkDTOS = new ArrayList<>();
        String line = reader.readLine();
        while (line != null){
            String[] parts = line.split("\\s+");
            linkDTOS.add(new LinkDTO(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]));
            line = reader.readLine();
        }
        return linkDTOS;
    }
}
