package net.pterodactylus.ams.core;

import static java.lang.String.format;

import java.io.IOException;
import java.util.List;

/**
 * Processes multiple {@link Command}s with a single {@link Session}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandProcessor {

	private final Session session;

	public CommandProcessor(Session session) {
		this.session = session;
	}

	public void process(Command command, List<String> arguments) throws IOException {
		command.process(session, arguments);
		session.getOutput().write(format("End of %s.\n", command.getName().toUpperCase()));
		session.getOutput().flush();
	}

}
