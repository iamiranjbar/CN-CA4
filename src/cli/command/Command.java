package cli.command;

import node.Node;

public interface Command {
    void execute(Node node) throws Exception;
}
