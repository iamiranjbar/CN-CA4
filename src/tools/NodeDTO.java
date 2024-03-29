package tools;

import java.util.ArrayList;

public class NodeDTO {
    private String name;
    private String ip;
    private int port;
    private ArrayList<LinkDTO> linkDTOS;

    public NodeDTO(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.linkDTOS = new ArrayList<>();
    }

    public void addLinks(ArrayList<LinkDTO> linkDTOS) {
        this.linkDTOS.addAll(linkDTOS);
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public ArrayList<LinkDTO> getLinkDTOS() {
        return linkDTOS;
    }

    @Override
    public String toString() {
        return "NodeDTO{\n\t" +
                "name= " + name + ",\n\t" +
                "ip=" + ip + ",\n\t" +
                "port=" + port + ",\n\t" +
                "linkDTOS=" + linkDTOS + "\n\t"+
                '}';
    }
}
