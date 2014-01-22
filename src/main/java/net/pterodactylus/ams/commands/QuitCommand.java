package net.pterodactylus.ams.commands;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Session;

/**
 * Simple {@link Command} that tells the {@link Session} to {@link
 * Session#exit() exit}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class QuitCommand implements Command {

	@Override
	public void process(Session session) {
		session.exit();
	}

}
