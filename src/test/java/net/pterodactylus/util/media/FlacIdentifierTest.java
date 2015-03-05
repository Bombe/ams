package net.pterodactylus.util.media;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
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
		File flacFile = createFile(new byte[] { 0x66, 0x4c, 0x61, 0x43 });
		MatcherAssert.assertThat(identifier.isMediaFile(flacFile), Matchers.is(true));
	}

	private File createFile(byte[] from) throws IOException {
		File tempFile = File.createTempFile("flac-identifier-", ".flac");
		tempFile.deleteOnExit();
		Files.write(from, tempFile);
		return tempFile;
	}

	@Test
	public void flacHeaderWithAMissingByteIsNotRecognized() throws IOException {
		File flacFile = createFile(new byte[] { 0x66, 0x4c, 0x61 });
		MatcherAssert.assertThat(identifier.isMediaFile(flacFile), Matchers.is(false));
	}

	@Test(expected = IOException.class)
	public void missingFileResultsInException() throws IOException {
		File flacFile = createFile(new byte[] { 0x66, 0x4c, 0x61, 0x43 });
		flacFile.delete();
		identifier.isMediaFile(flacFile);
	}

}
