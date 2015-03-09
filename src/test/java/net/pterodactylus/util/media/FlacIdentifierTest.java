package net.pterodactylus.util.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link FlacIdentifier}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FlacIdentifierTest {

	private final MediaFileIdentifier identifier = new FlacIdentifier();

	@Test
	public void flacHeaderIsRecognized() throws IOException {
		Path flacFile = createFile(new byte[] { 0x66, 0x4c, 0x61, 0x43 });
		MatcherAssert.assertThat(identifier.isMediaFile(flacFile), Matchers.is(true));
	}

	private Path createFile(byte[] from) throws IOException {
		Path tempFile = Files.createTempFile("flac-identifier-", ".flac");
		tempFile.toFile().deleteOnExit();
		Files.write(tempFile, from);
		return tempFile;
	}

	@Test
	public void flacHeaderWithAMissingByteIsNotRecognized() throws IOException {
		Path flacFile = createFile(new byte[] { 0x66, 0x4c, 0x61 });
		MatcherAssert.assertThat(identifier.isMediaFile(flacFile), Matchers.is(false));
	}

	@Test(expected = IOException.class)
	public void missingFileResultsInException() throws IOException {
		Path flacFile = createFile(new byte[] { 0x66, 0x4c, 0x61, 0x43 });
		Files.delete(flacFile);
		identifier.isMediaFile(flacFile);
	}

}
