package net.pterodactylus.ams.metadata;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import net.pterodactylus.ams.metadata.Metadata.FileType;

/**
 * Scans files for metadata.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MetadataScanner {

	public Optional<Metadata> scan(String filename) {
		if (filename.toLowerCase().endsWith(".mp3")) {
			return of(new Metadata(FileType.AUDIO));
		} else if (filename.toLowerCase().endsWith(".flac")) {
			return of(new Metadata(FileType.AUDIO));
		}
		return empty();
	}

}
