package net.pterodactylus.util.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagReader;

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
		Path file = Mockito.mock(Path.class);
		Tag tag = Mockito.mock(Tag.class);
		Mockito.when(tagReader.readTags(file)).thenReturn(Optional.of(tag));
		MatcherAssert.assertThat(identifier.isMediaFile(file), Matchers.is(true));
		Mockito.verify(tagReader).readTags(file);
	}

	@Test
	public void mp3SyncBitsAreRecognizedInFileIfNoTagIsRead() throws IOException {
		Path tempFile = createFakeMp3File();
		Mockito.when(tagReader.readTags(tempFile)).thenReturn(Optional.empty());
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(true));
		Mockito.verify(tagReader).readTags(tempFile);
	}

	private Path createFakeMp3File() throws IOException {
		return createFile(new byte[] { 0x00, (byte) 0xff, (byte) 0xff, 0x00 });
	}

	private Path createFile(byte[] from) throws IOException {
		Path tempFile = Files.createTempFile("mp3-identifier-", ".mp3");
		tempFile.toFile().deleteOnExit();
		Files.write(tempFile, from);
		return tempFile;
	}

	@Test
	public void fileShorterThan4KIsAbortedCleanly() throws IOException {
		Path tempFile = createShortFile();
		Mockito.when(tagReader.readTags(tempFile)).thenReturn(Optional.empty());
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(false));
		Mockito.verify(tagReader).readTags(tempFile);
	}

	private Path createShortFile() throws IOException {
		return createFile(new byte[] { 0x00 });
	}

	@Test
	public void fileLongerThan4KIsAbortedCleanly() throws IOException {
		Path tempFile = createLongFile();
		Mockito.when(tagReader.readTags(tempFile)).thenReturn(Optional.empty());
		MatcherAssert.assertThat(identifier.isMediaFile(tempFile), Matchers.is(false));
		Mockito.verify(tagReader).readTags(tempFile);
	}

	private Path createLongFile() throws IOException {
		return createFile(new byte[5120]);
	}

}
