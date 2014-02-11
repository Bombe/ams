package net.pterodactylus.util.tag.id3.v1;

import static java.time.LocalDate.ofYearDay;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;

import com.google.common.io.ByteStreams;
import org.junit.Test;

/**
 * Unit test for {@link ID3v1TagReader}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagReaderTest {

	private final ID3v1TagReader id3v1TagReader = new ID3v1TagReader();

	@Test
	public void readValidTagFromFile() throws IOException {
		File file = createFile("files/second.id3v1.mp3");
		Optional<Tag> tag = id3v1TagReader.readTags(file);
		assertThat(tag.isPresent(), is(true));
		assertThat(tag.get().getName(), is(of("Some Song")));
		assertThat(tag.get().getArtist(), is(of("Some Artist")));
		assertThat(tag.get().getAlbum(), is(of("Some Album")));
		assertThat(tag.get().getTrack(), is(of(1)));
		assertThat(tag.get().getDate(), is(of(ofYearDay(2014, 1))));
		assertThat(tag.get().getGenre(), is(empty()));
	}

	@Test
	public void dontReadTagFromFile() throws IOException {
		File file = createFile("files/c/second.vorbis.ogg");
		Optional<Tag> tag = id3v1TagReader.readTags(file);
		assertThat(tag.isPresent(), is(false));
	}

	@Test
	public void dontReadTagFromTooSmallFile() throws IOException {
		File file = createFile("files/test.unknown");
		Optional<Tag> tag = id3v1TagReader.readTags(file);
		assertThat(tag.isPresent(), is(false));
	}

	private File createFile(String resourceName) throws IOException {
		File temporaryFile = File.createTempFile("test-", ".dat");
		temporaryFile.deleteOnExit();
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName); OutputStream outputStream = new FileOutputStream(temporaryFile)) {
			ByteStreams.copy(inputStream, outputStream);
		}
		return temporaryFile;
	}

}
