package net.pterodactylus.util.tag.id3.v1;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * Unit test for {@link ID3v1TagRemover}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagRemoverTest {

	private final ID3v1TagRemover tagRemover = new ID3v1TagRemover();
	private final Path tempFile;

	public ID3v1TagRemoverTest() throws IOException {
		tempFile = Files.createTempFile("tag-remover-", ".test");
		tempFile.toFile().deleteOnExit();
	}

	@Test
	public void doNotChangeFileWithoutTag() throws IOException {
		prepareFileWithoutTag(tempFile);
		tagRemover.removeTag(tempFile);
		assertThat(Files.size(tempFile), is(400L));
	}

	@Test
	public void tagIsRemoved() throws IOException {
		prepareFileWithTag(tempFile);
		tagRemover.removeTag(tempFile);
		assertThat(Files.size(tempFile), is(400L));
	}

	private void prepareFileWithoutTag(Path tempFile) throws IOException {
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(tempFile, StandardOpenOption.WRITE)) {
			seekableByteChannel.write((ByteBuffer) ByteBuffer.allocate(400).put(new byte[400]).flip());
		}
	}

	private void prepareFileWithTag(Path tempFile) throws IOException {
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(tempFile, StandardOpenOption.WRITE)) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(400);
			byteBuffer.put(new byte[400]).flip();
			seekableByteChannel.write(byteBuffer);
			byteBuffer.compact().put("TAG".getBytes(StandardCharsets.UTF_8)).flip();
			seekableByteChannel.write(byteBuffer);
			byteBuffer.compact().put(new byte[125]).flip();
			seekableByteChannel.write(byteBuffer);
		}
	}

}
