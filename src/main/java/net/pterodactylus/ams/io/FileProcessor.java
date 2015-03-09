package net.pterodactylus.ams.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for objects that get called for {@link File}s found by the {@link
 * FileScanner}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
@FunctionalInterface
public interface FileProcessor {

	void processFile(Path file) throws IOException;

}
