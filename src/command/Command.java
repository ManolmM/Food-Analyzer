package command;

import java.util.List;

public interface Command {
    void executeRequest(List<String> commandArguments);
}
