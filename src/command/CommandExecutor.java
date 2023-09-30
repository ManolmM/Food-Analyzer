package command;

public class CommandExecutor {

    private CommandExecutor() {

    }

    public static CommandExecutor of() {
        return new CommandExecutor();
    }

}
