package net.pterodactylus.util.media;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * {@link MediaFileIdentifier} implementation that can detect AIFF and AIFC files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AiffIdentifier implements MediaFileIdentifier {

	@Override
	public boolean isMediaFile(Path file) throws IOException {
		try (InputStream fileInputStream = Files.newInputStream(file)) {
			if (invalidFormHeader(fileInputStream)) {
				return false;
			}
			ignore4Bytes(fileInputStream);
			return validAiffOrAifcHeader(fileInputStream);
		}
	}

	private boolean invalidFormHeader(InputStream fileInputStream) throws IOException {
		return fileInputStream.read() != 'F' || fileInputStream.read() != 'O' || fileInputStream.read() != 'R'
				|| fileInputStream.read() != 'M';
	}

	private void ignore4Bytes(InputStream fileInputStream) throws IOException {
		fileInputStream.read();
		fileInputStream.read();
		fileInputStream.read();
		fileInputStream.read();
	}

	private boolean validAiffOrAifcHeader(InputStream fileInputStream) throws IOException {
		if (fileInputStream.read() != 'A' || fileInputStream.read() != 'I' || fileInputStream.read() != 'F') {
			return false;
		}
		int lastByte = fileInputStream.read();
		return (lastByte == 'F') || (lastByte == 'C');
	}

}
