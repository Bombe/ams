package net.pterodactylus.ams.core;

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
		command.process(session);
	}

}
