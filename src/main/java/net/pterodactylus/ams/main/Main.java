package net.pterodactylus.ams.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import net.pterodactylus.ams.core.CommandDispatcher;
import net.pterodactylus.ams.core.CommandReader;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.core.commands.ListCommand;
import net.pterodactylus.ams.core.commands.LoadCommand;
import net.pterodactylus.ams.core.commands.QuitCommand;
import net.pterodactylus.ams.core.commands.SetAlbumArtistCommand;
import net.pterodactylus.ams.core.commands.SetAlbumCommand;
import net.pterodactylus.ams.core.commands.SetArtistCommand;
import net.pterodactylus.ams.core.commands.SetDateCommand;
import net.pterodactylus.ams.core.commands.SetDiscCommand;
import net.pterodactylus.ams.core.commands.SetNameCommand;
import net.pterodactylus.ams.core.commands.SetTotalDiscsCommand;
import net.pterodactylus.ams.core.commands.SetTrackCommand;
import net.pterodactylus.util.envopt.Parser;
import net.pterodactylus.util.envopt.SystemEnvironment;

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
		Options options = new Parser(new SystemEnvironment()).parseEnvironment(Options::new);
		try (Writer writer = new OutputStreamWriter(System.out);
			 Reader stdinReader = new InputStreamReader(System.in);
			 BufferedReader reader = new BufferedReader(stdinReader)) {
			Session session = new Session();
			Context context = new Context(options, session, writer);
			CommandDispatcher commandDispatcher = createCommandDispatcher(context);
			CommandReader commandReader = new CommandReader(commandDispatcher, reader, context);
			commandReader.run();
		}
	}

	private CommandDispatcher createCommandDispatcher(Context context) {
		CommandDispatcher commandDispatcher = new CommandDispatcher(context);
		commandDispatcher.addCommand(new QuitCommand());
		commandDispatcher.addCommand(new LoadCommand());
		commandDispatcher.addCommand(new ListCommand());
		commandDispatcher.addCommand(new SetArtistCommand());
		commandDispatcher.addCommand(new SetNameCommand());
		commandDispatcher.addCommand(new SetAlbumCommand());
		commandDispatcher.addCommand(new SetAlbumArtistCommand());
		commandDispatcher.addCommand(new SetTrackCommand());
		commandDispatcher.addCommand(new SetDiscCommand());
		commandDispatcher.addCommand(new SetTotalDiscsCommand());
		commandDispatcher.addCommand(new SetDateCommand());
		return commandDispatcher;
	}

	public static void main(String... arguments) throws IOException {
		new Main(arguments).run();
	}

}
