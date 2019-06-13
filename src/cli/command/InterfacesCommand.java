package cli.command;

import node.Node;

public class InterfacesCommand implements Command {

    public InterfacesCommand(){ }

    @Override
    public void execute() {
        Node.getInstance().showInterfacesInfo();
    }
}
