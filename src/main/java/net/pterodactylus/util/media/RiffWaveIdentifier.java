package net.pterodactylus.util.media;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * {@link MediaFileIdentifier} implementation that can detect Microsoft WAVE files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RiffWaveIdentifier implements MediaFileIdentifier {

	@Override
	public boolean isMediaFile(Path file) throws IOException {
		try (InputStream fileInputStream = Files.newInputStream(file)) {
			if (invalidRiffHeader(fileInputStream)) {
				return false;
			}
			ignore4Bytes(fileInputStream);
			if (invalidWaveHeader(fileInputStream)) {
				return false;
			}
			return true;
		}
	}

	private boolean invalidRiffHeader(InputStream fileInputStream) throws IOException {
		return fileInputStream.read() != 'R' || fileInputStream.read() != 'I' || fileInputStream.read() != 'F'
				|| fileInputStream.read() != 'F';
	}

	private void ignore4Bytes(InputStream fileInputStream) throws IOException {
		fileInputStream.read();
		fileInputStream.read();
		fileInputStream.read();
		fileInputStream.read();
	}

	private boolean invalidWaveHeader(InputStream fileInputStream) throws IOException {
		return fileInputStream.read() != 'W' || fileInputStream.read() != 'A' || fileInputStream.read() != 'V'
				|| fileInputStream.read() != 'E';
	}

}
