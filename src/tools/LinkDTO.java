package tools;

public class LinkDTO {
    HostDTO dst;
    String srcVip;
    String dstVip;

    public LinkDTO(String dstIp, int dstPort, String srcVip, String dstVip) {
        this.dst = new HostDTO(dstIp, dstPort);
        this.srcVip = srcVip;
        this.dstVip = dstVip;
    }

    @Override
    public String toString() {
        return "\n\t\t{" +
                "dst=" + dst.toString() + ",\n\t" +
                "srcVip='" + srcVip + ",\n\t" +
                "dstVip='" + dstVip + "\n\t" +
                '}';
    }
}
