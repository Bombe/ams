package net.pterodactylus.ams.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

import net.pterodactylus.ams.core.CommandDispatcher;
import net.pterodactylus.ams.core.CommandReader;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.core.commands.QuitCommand;

/**
 * Main starter class for AMS.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Main {

	private final String[] arguments;

	public Main(String[] arguments) {
		this.arguments = arguments;
	}

	private void run() throws IOException {
		try (Writer writer = System.console().writer();
			 BufferedReader reader = new BufferedReader(System.console().reader())) {
			Session session = new Session();
			Context context = new Context(session, writer);
			CommandDispatcher commandDispatcher = createCommandDispatcher(context);
			CommandReader commandReader = new CommandReader(commandDispatcher, reader, context);
			commandReader.run();
		}
	}

	private CommandDispatcher createCommandDispatcher(Context context) {
		CommandDispatcher commandDispatcher = new CommandDispatcher(context);
		commandDispatcher.addCommand(new QuitCommand());
		return commandDispatcher;
	}

	public static void main(String... arguments) throws IOException {
		new Main(arguments).run();
	}

}
