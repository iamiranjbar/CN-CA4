package tools;

import java.util.ArrayList;

public class HostDTO {
    private String ip;
    private int port;
    private ArrayList<LinkDTO> linkDTOS;

    public HostDTO(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.linkDTOS = new ArrayList<>();
    }

    public void addLinks(ArrayList<LinkDTO> linkDTOS) {
        this.linkDTOS.addAll(linkDTOS);
    }

    @Override
    public String toString() {
        return "HostDTO{\n\t" +
                "ip=" + ip + ",\n\t" +
                "port=" + port + ",\n\t" +
                "linkDTOS=" + linkDTOS + "\n\t"+
                '}';
    }
}
