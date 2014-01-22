package net.pterodactylus.ams.core;

import static java.lang.String.format;
import static net.pterodactylus.util.Tokenizer.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dispatches command from a command line.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandDispatcher {

	private final Map<String, Command> commands = new HashMap<>();
	private final Session session;
	private final CommandProcessor commandProcessor;

	public CommandDispatcher(Session session) {
		this.session = session;
		this.commandProcessor = new CommandProcessor(session);
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
			sendListOfValidTokens();
			return;
		}
		commandProcessor.process(command, tokens.subList(1, tokens.size()));
	}

	private void sendListOfValidTokens() throws IOException {
		session.getOutput().write("Valid commands are:");
		for (String command : commands.keySet()) {
			session.getOutput().write(format(" %s", command.toUpperCase()));
		}
		session.getOutput().write("\n");
		session.getOutput().flush();
	}

}
