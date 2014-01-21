package net.pterodactylus.ams.core;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandProcessor {

	private final Session session;

	public CommandProcessor(Session session) {
		this.session = session;
	}

	public void process(Command command) {
		command.process(session);
	}

}
