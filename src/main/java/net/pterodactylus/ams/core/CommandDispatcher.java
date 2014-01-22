package net.pterodactylus.ams.core;

import static net.pterodactylus.util.Tokenizer.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.pterodactylus.util.Tokenizer;

/**
 * Dispatches command from a command line.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandDispatcher {

	private final Map<String, Command> commands = new HashMap<>();
	private final CommandProcessor commandProcessor;

	public CommandDispatcher(CommandProcessor commandProcessor) {
		this.commandProcessor = commandProcessor;
	}

	public void addCommand(Command command) {
		commands.put(command.getName().toLowerCase(), command);
	}

	public void dispatch(String arguments) throws IOException {
		List<String> tokens = tokenize(arguments);
		if (tokens.isEmpty()) {
			return;
		}
		Command command = commands.get(tokens.get(0).toLowerCase());
		if (command == null) {
			return;
		}
		commandProcessor.process(command);
	}

}
