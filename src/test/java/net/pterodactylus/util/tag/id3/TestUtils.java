package net.pterodactylus.util.tag.id3;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Utility methods for testing.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TestUtils {

	public static Path createFile(String resourceName, Class<?> someClass) throws IOException {
		Path temporaryFile = Files.createTempFile("test-", ".dat");
		temporaryFile.toFile().deleteOnExit();
		try (InputStream inputStream = someClass.getClassLoader().getResourceAsStream(resourceName)) {
			Files.copy(inputStream, temporaryFile, StandardCopyOption.REPLACE_EXISTING);
		}
		return temporaryFile;
	}

}
