package net.pterodactylus.ams.main;

import static java.util.Arrays.asList;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import net.pterodactylus.ams.commands.ListCommand;
import net.pterodactylus.ams.commands.QuitCommand;
import net.pterodactylus.ams.core.CommandDispatcher;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.io.FileScanner;

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
		Session session = createSession();
		Console console = System.console();
		PrintWriter writer = console.writer();
		session.setOutput(writer);
		CommandDispatcher commandDispatcher = createCommandDispatcher(session);
		do {
			writer.write("> ");
			writer.flush();
			String line = console.readLine();
			if (line == null) {
				break;
			}
			commandDispatcher.dispatch(line);
		} while (!session.shouldExit());
	}

	private Session createSession() throws IOException {
		Session session;
		if (arguments.length == 0) {
			session = createSessionWithFiles(asList("."));
		} else {
			session = createSessionWithFiles(asList(arguments));
		}
		return session;
	}

	static Session createSessionWithFiles(List<String> files) throws IOException {
		Session session = new Session();
		for (String file : files) {
			FileScanner fileScanner = new FileScanner(new File(file));
			fileScanner.scan(session::addFile);
		}
		return session;
	}

	private CommandDispatcher createCommandDispatcher(Session session) {
		CommandDispatcher commandDispatcher = new CommandDispatcher(session);
		commandDispatcher.addCommand(new ListCommand());
		commandDispatcher.addCommand(new QuitCommand());
		return commandDispatcher;
	}

	public static void main(String... arguments) throws IOException {
		new Main(arguments).run();
	}

}
