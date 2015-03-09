package net.pterodactylus.util.tag.id3.v1;

import static java.time.LocalDate.ofYearDay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;

import org.junit.Test;

/**
 * Unit test for {@link ID3v1TagWriter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagWriterTest {

	private final ID3v1TagWriter tagWriter = new ID3v1TagWriter();
	private final ID3v1TagEncoder tagEncoder = new ID3v1TagEncoder();
	private final ID3v1TagReader tagReader = new ID3v1TagReader();
	private final Path tempFile;
	private final Tag tag = new Tag();

	public ID3v1TagWriterTest() throws IOException {
		tempFile = Files.createTempFile("tag-writer-", ".test");
		tempFile.toFile().deleteOnExit();
		tag.setName("Test Name");
		tag.setArtist("Test Artist");
		tag.setAlbum("Test Album");
		tag.setTrack(14);
		tag.setGenre("Metal");
		tag.setDate(ofYearDay(2014, 1));
		tag.setComment("Test Comment");
	}

	@Test
	public void canWriteTagToFileWithoutTag() throws IOException {
		tagWriter.write(tag, tempFile);
		verifyFileContainsTag(tempFile, 0, tagEncoder.encode(tag));
	}

	@Test
	public void canWriteToFileWithExistingTag() throws IOException {
		writeTagHeaderToFile(tempFile, 400);
		tagWriter.write(tag, tempFile);
		verifyFileContainsTag(tempFile, 400, tagEncoder.encode(tag));
	}

	private void writeTagHeaderToFile(Path file, int offset) throws IOException {
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(file, StandardOpenOption.WRITE)) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(offset);
			byteBuffer.put(new byte[offset]).flip();
			seekableByteChannel.write(byteBuffer);
			byteBuffer.compact().put("TAG".getBytes(StandardCharsets.UTF_8)).flip();
			seekableByteChannel.write(byteBuffer);
			byteBuffer.compact().put(new byte[125]).flip();
			seekableByteChannel.write(byteBuffer);
		}
	}

	private void verifyFileContainsTag(Path file, int offset, byte[] tag) throws IOException {
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(file)) {
			seekableByteChannel.position(offset);
			byte[] tagInFile = ID3v1Utils.readBuffer(seekableByteChannel);
			assertThat(tagInFile, is(tag));
			Optional<Tag> parsedTagFromFile = tagReader.readTags(file);
			assertThat(tagEncoder.encode(parsedTagFromFile.get()), is(tag));
		}
	}

	@Test
	public void emptyTagIsEncodable() {
		assertThat(tagWriter.isEncodable(tag), is(true));
	}

	@Test
	public void tagWithNameLongerThan30CharactersIsNotEncodable() {
		tag.setName("123456789012345678901234567890");
		assertThat(tagWriter.isEncodable(tag), is(true));
		tag.setName("1234567890123456789012345678901");
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithArtistLongerThan30CharactersIsNotEncodable() {
		tag.setArtist("123456789012345678901234567890");
		assertThat(tagWriter.isEncodable(tag), is(true));
		tag.setArtist("1234567890123456789012345678901");
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithAlbumArtistIsNotEncodable() {
		tag.setAlbumArtist("Album Artist");
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithAlbumLongerThan30CharactersIsNotEncodable() {
		tag.setAlbum("123456789012345678901234567890");
		assertThat(tagWriter.isEncodable(tag), is(true));
		tag.setAlbum("1234567890123456789012345678901");
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithNegativeTrackIsNotEncodable() {
		tag.setTrack(-1);
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithTooLargeTrackIsNotEncodable() {
		tag.setTrack(100);
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithTotalTracksIsNotEncodable() {
		tag.setTotalTracks(15);
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithDiscIsNotEncodable() {
		tag.setDisc(1);
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithTotalDiscsIsNotEncodable() {
		tag.setTotalDiscs(2);
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithUnknownGenreIsNotEncodable() {
		tag.setGenre("Hugendubel");
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithCommentLongerThan30CharactersIsNotEncodable() {
		tag.setTrack(0);
		tag.setComment("123456789012345678901234567890");
		assertThat(tagWriter.isEncodable(tag), is(true));
		tag.setComment("1234567890123456789012345678901");
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

	@Test
	public void tagWithCommentLongerThan28CharactersAndATrackNumberIsNotEncodable() {
		tag.setTrack(13);
		tag.setComment("12345678901234567890123456789");
		assertThat(tagWriter.isEncodable(tag), is(false));
	}

}
