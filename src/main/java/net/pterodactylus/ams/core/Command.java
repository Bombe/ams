package net.pterodactylus.ams.core;

/**
 * Interface for commands.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Command {

	void process(Session session);

}
