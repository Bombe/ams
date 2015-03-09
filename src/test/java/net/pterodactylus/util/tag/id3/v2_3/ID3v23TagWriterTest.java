package net.pterodactylus.util.tag.id3.v2_3;

import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
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
	private final Path mp3File;

	public ID3v23TagWriterTest() throws IOException {
		mp3File = createMp3File();
	}

	@Test
	public void nonMp3FileIsNotChanged() throws IOException {
		Path nonMp3File = Files.createTempFile("non-mp3-", ".dat");
		nonMp3File.toFile().delete();
		Files.write(nonMp3File, "NotAnMp3".getBytes(StandardCharsets.UTF_8));
		tagWriter.write(tag, nonMp3File);
		assertThat(Files.readAllBytes(nonMp3File), is("NotAnMp3".getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void mp3FileIsChanged() throws IOException {
		tagWriter.write(tag, mp3File);
		assertThat(Files.readAllBytes(mp3File).length, greaterThan(2));
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

	@Test
	public void discNumberIsCorrectlyWrittenToFile() throws IOException {
		tag.setDisc(1);
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getDisc(), is(of(1)));
	}

	@Test
	public void totalDiscNumberIsCorrectlyWrittenToFile() throws IOException {
		tag.setTotalDiscs(2);
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getTotalDiscs(), is(of(2)));
	}

	@Test
	public void dateIsCorrectlyWrittenToFile() throws IOException {
		tag.setDate(LocalDate.of(2014, 9, 28));
		tagWriter.write(tag, mp3File);
		Optional<Tag> readTag = new ID3v23TagReader().readTags(mp3File);
		assertThat(readTag.get().getDate(), is(of(LocalDate.of(2014, 9, 28))));
	}

	private Path createMp3File() throws IOException {
		Path mp3File = Files.createTempFile("mp3-", ".mp3");
		mp3File.toFile().deleteOnExit();
		writeMp3FrameHeaderToFile(mp3File);
		return mp3File;
	}

	private void writeMp3FrameHeaderToFile(Path mp3File) throws IOException {
		try (OutputStream fileOutput = Files.newOutputStream(mp3File)) {
			fileOutput.write(0xff);
			fileOutput.write(0xe0);
		}
	}

}
