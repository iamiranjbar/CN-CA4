package cli.command;

import node.Node;

public class UpCommand implements Command {
	
	private int id;
	
	public UpCommand(int id) {
		this.id = id;
	}

	public void execute(Node node) throws Exception {
		node.upInterface(id);
	}

}
