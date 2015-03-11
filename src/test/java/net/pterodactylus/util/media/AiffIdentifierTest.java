package net.pterodactylus.util.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link AiffIdentifier}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AiffIdentifierTest {

	private final MediaFileIdentifier aiffIdentifier = new AiffIdentifier();

	@Test
	public void aiffHeaderIsRecognized() throws IOException {
		Path tempFile = createFile(new byte[] { 'F', 'O', 'R', 'M', 0x00, 0x00, 0x00, 0x00, 'A', 'I', 'F', 'F' });
		MatcherAssert.assertThat(aiffIdentifier.isMediaFile(tempFile), Matchers.is(true));
	}

	@Test
	public void aifcHeaderIsRecognized() throws IOException {
		Path tempFile = createFile(new byte[] { 'F', 'O', 'R', 'M', 0x00, 0x00, 0x00, 0x00, 'A', 'I', 'F', 'C' });
		MatcherAssert.assertThat(aiffIdentifier.isMediaFile(tempFile), Matchers.is(true));
	}

	@Test
	public void incorrectAiffHeaderIsRecognized() throws IOException {
		Path tempFile = createFile(new byte[] { 'F', 'O', 'R', 'M', 0x00, 0x00, 0x00, 0x00, 'R', 'I', 'F', 'T' });
		MatcherAssert.assertThat(aiffIdentifier.isMediaFile(tempFile), Matchers.is(false));
	}

	private Path createFile(byte[] from) throws IOException {
		Path tempFile = Files.createTempFile("flac-identifier-", ".flac");
		tempFile.toFile().deleteOnExit();
		Files.write(tempFile, from);
		return tempFile;
	}

	@Test
	public void incorrectFormHeaderIsRecognized() throws IOException {
		Path tempFile = createFile(new byte[] { 'F', 'A', 'R', 'M', 0x00, 0x00, 0x00, 0x00, 'A', 'I', 'F', 'F' });
		MatcherAssert.assertThat(aiffIdentifier.isMediaFile(tempFile), Matchers.is(false));
	}

}
