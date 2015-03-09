package net.pterodactylus.util.media;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * {@link MediaFileIdentifier} that can identify FLAC files. The file is only checked for the “fLaC” header, as detailed
 * by <a href="https://xiph.org/flac/format.html">xiph.org/flac/format.html</a>.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FlacIdentifier implements MediaFileIdentifier {

	@Override
	public boolean isMediaFile(Path file) throws IOException {
		try (InputStream inputStream = Files.newInputStream(file)) {
			return hasFlacHeader(inputStream);
		}
	}

	private boolean hasFlacHeader(InputStream inputStream) throws IOException {
		return inputStream.read() == 'f' && inputStream.read() == 'L' && inputStream.read() == 'a'
				&& inputStream.read() == 'C';
	}

}
