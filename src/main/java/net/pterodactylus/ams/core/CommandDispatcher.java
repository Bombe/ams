package net.pterodactylus.ams.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages {@link Command}s and dispatches calls with parameters.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandDispatcher {

	private final Map<String, Command> commands = new HashMap<>();
	private final Context context;

	public CommandDispatcher(Context context) {
		this.context = context;
	}

	public void addCommand(Command command) {
		commands.put(command.getName().toLowerCase(), command);
	}

	public void runCommand(String commandName, List<String> parameters) throws IOException {
		Command command = commands.get(commandName);
		if (command == null) {
			context.write("Invalid command.\n");
			context.flush();
			return;
		}
		command.execute(context, parameters);
	}

}
