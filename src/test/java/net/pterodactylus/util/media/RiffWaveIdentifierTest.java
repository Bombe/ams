package net.pterodactylus.util.media;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link RiffWaveIdentifierTest}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RiffWaveIdentifierTest {

	private final RiffWaveIdentifier identifier = new RiffWaveIdentifier();

	@Test
	public void correctHeaderIsRecognized() throws IOException {
		File tempFile = createFile(new byte[] { 'R', 'I', 'F', 'F', 0x00, 0x00, 0x00, 0x00, 'W', 'A', 'V', 'E' });
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(true));
	}

	@Test
	public void invalidWaveChunkIsDetected() throws IOException {
		File tempFile = createFile(new byte[] { 'R', 'I', 'F', 'F', 0x00, 0x00, 0x00, 0x00, 'W', 'A', 'V' });
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(false));
	}

	@Test
	public void invalidRiffChunkIsDetected() throws IOException {
		File tempFile = createFile(new byte[] { 'R', 'I', 'F', 'T', 0x00, 0x00, 0x00, 0x00, 'W', 'A', 'V' });
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(false));
	}

	private File createFile(byte[] from) throws IOException {
		File tempFile = File.createTempFile("mp3-identifier-", ".mp3");
		tempFile.deleteOnExit();
		Files.write(from, tempFile);
		return tempFile;
	}


}
