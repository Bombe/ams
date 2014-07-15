package net.pterodactylus.ams.conversion;

import static java.io.File.createTempFile;
import static java.nio.file.Files.readAllBytes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import org.junit.Test;

/**
 * Unit test for {@link FileSink}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileSinkTest {

	private final File tempFile;
	private final FileSink fileSink;

	public FileSinkTest() throws IOException {
		tempFile = createTempFile("file-sink-", ".dat");
		tempFile.deleteOnExit();
		fileSink = new FileSink(tempFile);
	}

	@Test
	public void dataWrittenToFileSinkAppearsInFile() throws IOException {
		byte[] randomBytes = new byte[4096];
		try (OutputStream outputStream = fileSink.getOutputStream()) {
			new Random().nextBytes(randomBytes);
			outputStream.write(randomBytes);
		}
		byte[] readBytes = readAllBytes(tempFile.toPath());
		assertThat(readBytes, is(randomBytes));
	}

	@Test
	public void toStringContainsFileSinkAndNameOfFile() throws IOException {
		assertThat(fileSink.toString(), containsString("sink:"));
		assertThat(fileSink.toString(), containsString("file:"));
		assertThat(fileSink.toString(), containsString(tempFile.getName()));
	}

}
