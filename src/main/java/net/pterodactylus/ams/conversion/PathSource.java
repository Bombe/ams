package net.pterodactylus.ams.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A {@link Source} which reads its source data from a {@link Path}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PathSource implements Source {

	private final Path sourceFile;

	public PathSource(Path sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return Files.newInputStream(sourceFile);
	}

	@Override
	public String toString() {
		return "source:file://" + sourceFile.toAbsolutePath().toString();
	}

}
