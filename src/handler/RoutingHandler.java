package handler;

import ip.IPDatagaram;
import node.Interface;
import node.Node;
import routing.DV;
import java.io.IOException;

public class RoutingHandler {

    public static void run(Node node, IPDatagaram ipDatagaram) {
        try {
            DV newDV = new DV(ipDatagaram.getData());
            int interfaceId = node.findInterfaceId(ipDatagaram.getDstAddress());
            Interface face = node.getInterfaceById(interfaceId);
            if (newDV.getCostTo(ipDatagaram.getDstAddress()) > node.getMaxCost()) {
                node.downInterface(interfaceId);
//                System.out.println("ok " + interfaceId);
            } else if (newDV.getCostTo(ipDatagaram.getDstAddress()) <= node.getMaxCost() &&
                    !face.isEnable()) {
                node.upInterface(interfaceId);
//                System.out.println("qd " + interfaceId);
            }
            node.updateRoutingTables(newDV, interfaceId, ipDatagaram);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
