package net.pterodactylus.ams.commands;

import static java.lang.String.format;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Session;

/**
 * Shows or sets album information in a {@link Session}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AlbumCommand implements Command {

	@Override
	public void process(Session session, List<String> parameters) throws IOException {
		if (parameters.isEmpty()) {
			dumpAlbumInformation(session);
		} else {
			setAlbumFromParameters(session, parameters);
		}
	}

	private void setAlbumFromParameters(Session session, List<String> parameters) throws IOException {
		StringJoiner stringJoiner = new StringJoiner(" ");
		parameters.stream().forEach(stringJoiner::add);
		session.setAlbum(stringJoiner.toString());
		session.getOutput().write(format("Album set to: %s\n", stringJoiner.toString()));
	}

	private void dumpAlbumInformation(Session session) throws IOException {
		if (session.getAlbum().isPresent()) {
			session.getOutput().write(format("Album: %s\n", session.getAlbum().get()));
		} else {
			session.getOutput().write("No album set.\n");
		}
	}

}
