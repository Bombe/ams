package net.pterodactylus.ams.conversion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link Sink} that writes all received data to a {@link File}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileSink implements Sink {

	private final File destinationFile;

	public FileSink(File destinationFile) {
		this.destinationFile = destinationFile;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(destinationFile);
	}

	@Override
	public String toString() {
		return "sink:file://" + destinationFile.getAbsolutePath();
	}

}
