package cli.command;

import node.Node;

public class QuitCommand implements Command {
    @Override
    public void execute(Node node) throws Exception {
        node.quit();
    }
}
