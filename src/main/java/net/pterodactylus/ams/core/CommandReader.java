package net.pterodactylus.ams.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
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
	private final BufferedReader reader;
	private final Context context;
	private final Deque<String> additionalLines = new LinkedList<>();

	public CommandReader(CommandDispatcher commandDispatcher, BufferedReader reader, Context context) {
		this.commandDispatcher = commandDispatcher;
		this.reader = reader;
		this.context = context;
	}

	public void addLine(String line) {
		synchronized (additionalLines) {
			additionalLines.addLast(line);
		}
	}

	@Override
	public void run() {
		try {
			while (!context.shouldExit()) {
				try {
					String line = getNextLine();
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

	private String getNextLine() throws IOException {
		synchronized (additionalLines) {
			if (!additionalLines.isEmpty()) {
				return additionalLines.removeFirst();
			}
		}
		context.write("> ");
		context.flush();
		return reader.readLine();
	}

}
