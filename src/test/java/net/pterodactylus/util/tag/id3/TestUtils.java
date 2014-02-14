package net.pterodactylus.util.tag.id3;

import static com.google.common.io.ByteStreams.copy;
import static java.io.File.createTempFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods for testing.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TestUtils {

	public static File createFile(String resourceName, Class<?> someClass) throws IOException {
		File temporaryFile = createTempFile("test-", ".dat");
		temporaryFile.deleteOnExit();
		try (InputStream inputStream = someClass.getClassLoader().getResourceAsStream(resourceName); OutputStream outputStream = new FileOutputStream(temporaryFile)) {
			copy(inputStream, outputStream);
		}
		return temporaryFile;
	}

}
