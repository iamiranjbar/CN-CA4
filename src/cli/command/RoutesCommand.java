package cli.command;

import node.Node;

public class RoutesCommand implements Command{

	@Override
	public void execute(Node node) {
		node.showForwardingTable();
		node.showDV();
	}

}
