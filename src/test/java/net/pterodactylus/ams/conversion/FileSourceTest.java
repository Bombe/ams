package net.pterodactylus.ams.conversion;

import static com.google.common.io.ByteStreams.toByteArray;
import static java.io.File.createTempFile;
import static java.nio.file.Files.write;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.junit.Test;

/**
 * Unit test for {@link FileSource}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileSourceTest {

	private final File tempFile;
	private final FileSource fileSource;

	public FileSourceTest() throws IOException {
		tempFile = createTempFile("file-source-", ".dat");
		tempFile.deleteOnExit();
		fileSource = new FileSource(tempFile);
	}

	@Test
	public void fileCanBeRead() throws IOException {
		byte[] randomBytes = new byte[4096];
		new Random().nextBytes(randomBytes);
		write(tempFile.toPath(), randomBytes);
		try (InputStream inputStream = fileSource.getInputStream()) {
			byte[] readBytes = toByteArray(inputStream);
			assertThat(readBytes, is(randomBytes));
		}
	}

	@Test
	public void toStringContainsFileSinkAndNameOfFile() throws IOException {
		assertThat(fileSource.toString(), containsString("source:"));
		assertThat(fileSource.toString(), containsString("file:"));
		assertThat(fileSource.toString(), containsString(tempFile.getName()));
	}

}
