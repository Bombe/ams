package net.pterodactylus.ams.conversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A {@link Source} which reads its source data from a {@link File}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileSource implements Source {

	private final File sourceFile;

	public FileSource(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(sourceFile);
	}

	@Override
	public String toString() {
		return "source:file://" + sourceFile.getAbsolutePath();
	}

}
