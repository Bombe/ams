package net.pterodactylus.ams.commands;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.metadata.Metadata;
import net.pterodactylus.ams.metadata.MetadataScanner;

/**
 * Scans all files in the session for meta information.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ScanCommand implements Command {

	private final MetadataScanner metadataScanner = new MetadataScanner();

	@Override
	public void process(Session session, List<String> parameters) throws IOException {
		for (File file : session.getFiles(parameters)) {
			Optional<Metadata> metadata = metadataScanner.scan(file.getPath());
			if (!metadata.isPresent()) {
				continue;
			}
			session.getOutput().write(format("%s: %s\n", metadata.get().getType(), file.getPath()));
		}
	}

}
