package cli.command;

import node.Node;

public class InterfacesCommand implements Command {

    public InterfacesCommand(){ }

    public void execute(Node node) {
        node.showInterfacesInfo();
    }
}
