package net.pterodactylus.ams.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import net.pterodactylus.ams.core.LineParser.EmptyLine;
import net.pterodactylus.ams.core.LineParser.Result;

/**
 * Reads lines from a {@link BufferedReader} and uses a {@link CommandDispatcher} to run read commands.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandReader implements Runnable {

	private final LineParser lineParser = new LineParser();
	private final CommandDispatcher commandDispatcher;
	private final Context context;

	public CommandReader(CommandDispatcher commandDispatcher, Context context) {
		this.commandDispatcher = commandDispatcher;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			while (!context.shouldExit()) {
				try {
					String line = context.getNextLine();
					Optional<Result> result = lineParser.parse(line);
					if (!result.isPresent()) {
						continue;
					}
					commandDispatcher.runCommand(result.get().getCommand(), result.get().getParameters());
				} catch (EmptyLine el1) {
					throw el1;
				} catch (RuntimeException re1) {
					/* ignore, continue. */
					context.write(re1.getClass().getName() + "\n");
					continue;
				}
			}
		} catch (EmptyLine | IOException e) {
			/* just end this. */
		}
	}

}
