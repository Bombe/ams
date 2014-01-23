package net.pterodactylus.ams.metadata;

import static java.util.Arrays.asList;
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
		Optional<String> extension = getExtension(filename);
		if (extension.isPresent() && asList("mp3", "flac", "ogg").contains(extension.get())) {
			return of(new Metadata(FileType.AUDIO));
		}
		return empty();
	}

	private Optional<String> getExtension(String filename) {
		int lastDot = filename.lastIndexOf('.');
		return (lastDot == -1) ? empty() : of(filename.substring(lastDot + 1).toLowerCase());
	}

}
