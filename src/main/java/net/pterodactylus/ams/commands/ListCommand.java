package net.pterodactylus.ams.commands;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Session;

/**
 * {@link Command} that lists all {@link Session#getFiles() files} of a
 * session.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ListCommand implements Command {

	@Override
	public void process(Session session) throws IOException {
		for (File file : session.getFiles()) {
			session.getOutput().write(format("%s\n", file.getAbsolutePath()));
		}
		session.getOutput().write("End of LIST.\n");
	}

}
