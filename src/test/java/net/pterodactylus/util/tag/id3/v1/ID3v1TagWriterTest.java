package net.pterodactylus.util.tag.id3.v1;

import static java.io.File.createTempFile;
import static java.time.LocalDate.ofYearDay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
	private final File tempFile;
	private final ID3v1Tag tag;

	public ID3v1TagWriterTest() throws IOException {
		tempFile = createTempFile("tag-writer-", ".test");
		tempFile.deleteOnExit();
		tag = new ID3v1Tag();
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
		tagWriter.writeTag(tempFile, tag);
		verifyFileContainsTag(tempFile, 0, tagEncoder.encode(tag));
	}

	@Test
	public void canWriteToFileWithExistingTag() throws IOException {
		writeTagHeaderToFile(tempFile, 400);
		tagWriter.writeTag(tempFile, tag);
		verifyFileContainsTag(tempFile, 400, tagEncoder.encode(tag));
	}

	private void writeTagHeaderToFile(File file, int offset) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
			randomAccessFile.write(new byte[offset]);
			randomAccessFile.write("TAG".getBytes("UTF-8"));
			randomAccessFile.write(new byte[125]);
		}
	}

	private void verifyFileContainsTag(File file, int offset, byte[] tag) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
			randomAccessFile.seek(offset);
			byte[] tagInFile = ID3v1Utils.readBuffer(randomAccessFile);
			assertThat(tagInFile, is(tag));
			Optional<Tag> parsedTagFromFile = tagReader.readTags(file);
			assertThat(tagEncoder.encode(parsedTagFromFile.get()), is(tag));
		}
	}

}
