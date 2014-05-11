package net.pterodactylus.util.tag.id3.v2_3;

import static com.google.common.io.Files.toByteArray;
import static com.google.common.io.Files.write;
import static java.io.File.createTempFile;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;

import org.junit.Test;

/**
 * Unit test for {@link ID3v23TagWriter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v23TagWriterTest {

	private final ID3v23TagWriter tagWriter = new ID3v23TagWriter();
	private final Tag tag = new Tag();
	private final File mp3File;

	public ID3v23TagWriterTest() throws IOException {
		mp3File = createMp3File();
	}

	@Test
	public void nonMp3FileIsNotChanged() throws IOException {
		File nonMp3File = createTempFile("non-mp3-", ".dat");
		nonMp3File.deleteOnExit();
		write("NotAnMp3", nonMp3File, UTF_8);
		tagWriter.write(tag, nonMp3File);
		assertThat(toByteArray(nonMp3File), is("NotAnMp3".getBytes(UTF_8)));
	}

	@Test
	public void mp3FileIsChanged() throws IOException {
		tagWriter.write(tag, mp3File);
		assertThat(toByteArray(mp3File).length, greaterThan(2));
	}

	@Test
	public void artistIsCorrectlyWrittenToFile() throws IOException {
		tag.setArtist("Some Ärtist");
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getArtist(), is(of("Some Ärtist")));
	}

	@Test
	public void titleIsCorrectlyWrittenToFile() throws IOException {
		tag.setName("Som€ Name");
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getName(), is(of("Som€ Name")));
	}

	@Test
	public void albumIsCorrectlyWrittenToFile() throws IOException {
		tag.setAlbum("Some Album");
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getAlbum(), is(of("Some Album")));
	}

	@Test
	public void albumArtistIsCorrectlyWrittenToFile() throws IOException {
		tag.setAlbumArtist("Some Album Artist");
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getAlbumArtist(), is(of("Some Album Artist")));
	}

	@Test
	public void trackNumberIsCorrectlyWrittenToFile() throws IOException {
		tag.setTrack(17);
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getTrack(), is(of(17)));
	}

	@Test
	public void totalTrackNumberIsCorrectlyWrittenToFile() throws IOException {
		tag.setTotalTracks(23);
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getTotalTracks(), is(of(23)));
	}

	private File createMp3File() throws IOException {
		File mp3File = createTempFile("mp3-", ".mp3");
		writeMp3FrameHeaderToFile(mp3File);
		mp3File.deleteOnExit();
		return mp3File;
	}

	private void writeMp3FrameHeaderToFile(File mp3File) throws IOException {
		try (OutputStream fileOutput = new FileOutputStream(mp3File)) {
			fileOutput.write(0xff);
			fileOutput.write(0xe0);
		}
	}

}
