package net.pterodactylus.ams.core;

import java.io.IOException;

/**
 * Interface for commands.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Command {

	default String getName() {
		return getClass().getSimpleName().replaceAll("Command$", "");
	}

	void process(Session session) throws IOException;

}
