package net.pterodactylus.ams.core;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

import java.io.IOException;

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

	public void process(Command command) throws IOException {
		command.process(session, emptyList());
		session.getOutput().write(format("End of %s.\n", command.getName().toUpperCase()));
		session.getOutput().flush();
	}

}
