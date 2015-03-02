package net.pterodactylus.util.media;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagReader;

import com.google.common.io.Files;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link Mp3IdentifierTest}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Mp3IdentifierTest {

	private final TagReader tagReader = Mockito.mock(TagReader.class);
	private final Mp3Identifier identifier = new Mp3Identifier(tagReader);

	@Test
	public void canCreateDefaultMp3Identifier() {
		new Mp3Identifier();
	}

	@Test
	public void tagReaderIsConsultedFirst() throws IOException {
		File file = Mockito.mock(File.class);
		Tag tag = Mockito.mock(Tag.class);
		Mockito.when(tagReader.readTags(file)).thenReturn(Optional.of(tag));
		MatcherAssert.assertThat(identifier.isMediaFile(file), Matchers.is(true));
		Mockito.verify(tagReader).readTags(file);
	}

	@Test
	public void mp3SyncBitsAreRecognizedInFileIfNoTagIsRead() throws IOException {
		File tempFile = createFakeMp3File();
		Mockito.when(tagReader.readTags(tempFile)).thenReturn(Optional.empty());
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(true));
		Mockito.verify(tagReader).readTags(tempFile);
	}

	private File createFakeMp3File() throws IOException {
		return createFile(new byte[] { 0x00, (byte) 0xff, (byte) 0xff, 0x00 });
	}

	private File createFile(byte[] from) throws IOException {
		File tempFile = File.createTempFile("mp3-identifier-", ".mp3");
		tempFile.deleteOnExit();
		Files.write(from, tempFile);
		return tempFile;
	}

	@Test
	public void fileShorterThan4KIsAbortedCleanly() throws IOException {
		File tempFile = createShortFile();
		Mockito.when(tagReader.readTags(tempFile)).thenReturn(Optional.empty());
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(false));
		Mockito.verify(tagReader).readTags(tempFile);
	}

	private File createShortFile() throws IOException {
		return createFile(new byte[] { 0x00 });
	}

	@Test
	public void fileLongerThan4KIsAbortedCleanly() throws IOException {
		File tempFile = createLongFile();
		Mockito.when(tagReader.readTags(tempFile)).thenReturn(Optional.empty());
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(false));
		Mockito.verify(tagReader).readTags(tempFile);
	}

	private File createLongFile() throws IOException {
		return createFile(new byte[5120]);
	}

}
