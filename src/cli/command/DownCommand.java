package cli.command;

import node.Node;

public class DownCommand implements Command {

	private int id;
	
	public DownCommand(int id) {
		this.id = id;
	}
	
	public void execute(Node node) throws Exception {
		node.downInterface(id);
	}

}
