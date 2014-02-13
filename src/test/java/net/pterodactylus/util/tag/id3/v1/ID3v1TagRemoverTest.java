package net.pterodactylus.util.tag.id3.v1;

import static java.io.File.createTempFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Test;

/**
 * Unit test for {@link ID3v1TagRemover}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagRemoverTest {

	private final ID3v1TagRemover tagRemover = new ID3v1TagRemover();
	private final File tempFile;

	public ID3v1TagRemoverTest() throws IOException {
		tempFile = createTempFile("tag-remover-", ".test");
		tempFile.deleteOnExit();
	}

	@Test
	public void doNotChangeFileWithoutTag() throws IOException {
		prepareFileWithoutTag(tempFile);
		tagRemover.removeTag(tempFile);
		assertThat(tempFile.length(), is(400L));
	}

	@Test
	public void tagIsRemoved() throws IOException {
		prepareFileWithTag(tempFile);
		tagRemover.removeTag(tempFile);
		assertThat(tempFile.length(), is(400L));
	}

	private void prepareFileWithoutTag(File tempFile) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw")) {
			randomAccessFile.write(new byte[400]);
		}
	}

	private void prepareFileWithTag(File tempFile) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw")) {
			randomAccessFile.write(new byte[400]);
			randomAccessFile.write("TAG".getBytes("UTF-8"));
			randomAccessFile.write(new byte[125]);
		}
	}

}
