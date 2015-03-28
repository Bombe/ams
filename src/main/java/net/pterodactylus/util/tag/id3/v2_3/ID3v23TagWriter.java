package net.pterodactylus.util.tag.id3.v2_3;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.v2_3.Header.parseHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import net.pterodactylus.util.media.Mp3Identifier;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagWriter;

/**
 * Writes an ID3v2 version 2.3 tag to a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v23TagWriter implements TagWriter {

	private final Mp3Identifier mp3Identifier = new Mp3Identifier();
	private final TagEncoder tagEncoder = new TagEncoder();

	@Override
	public boolean isEncodable(Tag tag) {
		// TODO
		return false;
	}

	@Override
	public void write(Tag tag, Path file) throws IOException {
		Optional<Integer> headerSize;
		try (InputStream inputStream = Files.newInputStream(file)) {
			Optional<Header> header = parseHeader(inputStream);
			headerSize = header.isPresent() ? of(header.get().getSize()) : empty();
		}
		byte[] encodedTag = tagEncoder.encode(tag, headerSize);
		if (headerSize.isPresent()) {
			if ((headerSize.get() + 10) > encodedTag.length) {
				overwriteTag(headerSize.get() + 10, encodedTag, file);
			} else {
				removeOldTagAndWriteNewTag(headerSize.get() + 10, encodedTag, file);
			}
		} else if (mp3Identifier.isMediaFile(file)) {
			insertNewTag(encodedTag, file);
		}
	}

	private void overwriteTag(int oldTagSize, byte[] encodedTag, Path file) {
		// TODO
	}

	private void removeOldTagAndWriteNewTag(int oldTagSize, byte[] encodedTag, Path file) {
		// TODO
	}

	private void insertNewTag(byte[] encodedTag, Path file) throws IOException {
		int paddingSize = calculatePaddingSize(encodedTag.length);
		Path taggedFile = Files.createTempFile(file.getParent(), "tagged-", ".mp3");
		try (OutputStream taggedFileOutputStream = Files.newOutputStream(taggedFile)) {
			taggedFileOutputStream.write(encodedTag);
			taggedFileOutputStream.write(new byte[paddingSize - encodedTag.length]);
			Files.copy(file, taggedFileOutputStream);
		} catch (IOException ioe1) {
			Files.deleteIfExists(taggedFile);
			throw ioe1;
		}
		try {
			Files.move(taggedFile, file, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe1) {
			Files.deleteIfExists(taggedFile);
			throw ioe1;
		}
	}

	private int calculatePaddingSize(int tagLength) {
		return 1024 * (((tagLength + 1536) / 1024) + 1);
	}

}
