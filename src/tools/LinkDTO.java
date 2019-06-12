package tools;

public class LinkDTO {
    private NodeDTO dst;
    private String srcVip;
    private String dstVip;

    public LinkDTO(String dstIp, int dstPort, String srcVip, String dstVip) {
        this.dst = new NodeDTO(null, dstIp, dstPort);
        this.srcVip = srcVip;
        this.dstVip = dstVip;
    }

    public NodeDTO getDst() {
        return dst;
    }

    public String getSrcVip() {
        return srcVip;
    }

    public String getDstVip() {
        return dstVip;
    }

    @Override
    public String toString() {
        return "{" +
                "dst=" + dst.toString() + ",\n\t" +
                "srcVip='" + srcVip + ",\n\t" +
                "dstVip='" + dstVip + "\n\t" +
                '}';
    }
}