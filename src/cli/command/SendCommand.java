package cli.command;

import node.Node;

public class SendCommand implements Command {

    private String dstVIp;
    private int protocolNum;
    private String payload;

    public SendCommand(String dstVIp, int protocolNum, String payload) {
        this.dstVIp = dstVIp;
        this.protocolNum = protocolNum;
        this.payload = payload;
    }

    @Override
    public void execute(Node node) throws Exception {
        node.sendData(dstVIp, protocolNum, payload);
    }

}
