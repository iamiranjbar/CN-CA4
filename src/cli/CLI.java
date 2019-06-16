package cli;

import cli.command.*;
import node.Node;
import tools.StringArrayCollector;

import java.util.Scanner;

public class CLI implements Runnable {
    private static final Scanner reader = new Scanner(System.in);
    private Node node;

    public CLI(Node node) {
    	this.node = node;
    }
    
    public String getCommand() {
        return reader.nextLine();
    }

    public String[] parseCommand(String command) {
        return command.split("\\s+");
    }

    public Command createCommand(String[] splittedCommand) throws Exception {
        switch (splittedCommand[0]){
            case "interfaces":
                return new InterfacesCommand();
            case "routes":
                return new RoutesCommand();
            case "down":
                return new DownCommand(Integer.parseInt(splittedCommand[1]));
            case "up":
            	return new UpCommand(Integer.parseInt(splittedCommand[1]));
            case "send":
                return new SendCommand(splittedCommand[1], Integer.parseInt(splittedCommand[2]),
                        StringArrayCollector.join(splittedCommand, " ", 3, splittedCommand.length));
            default:
                throw new Exception("Wrong command!");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String[] splittedCommands = this.parseCommand(this.getCommand());
                Command command = this.createCommand(splittedCommands);
                command.execute(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
