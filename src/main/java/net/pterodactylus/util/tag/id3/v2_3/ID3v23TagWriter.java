package net.pterodactylus.util.tag.id3.v2_3;

import static com.google.common.io.Files.copy;
import static java.io.File.createTempFile;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.v2_3.Header.parseHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagWriter;

/**
 * Writes an ID3v2 version 2.3 tag to a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v23TagWriter implements TagWriter {

	private final TagEncoder tagEncoder = new TagEncoder();

	@Override
	public boolean isEncodable(Tag tag) {
		// TODO
		return false;
	}

	@Override
	public boolean write(Tag tag, File file) throws IOException {
		Optional<Integer> headerSize;
		try (InputStream inputStream = new FileInputStream(file)) {
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
		} else {
			if (!fileIsAnMp3File(file)) {
				return false;
			}
			insertNewTag(encodedTag, file);
		}
		return true;
	}

	private void overwriteTag(int oldTagSize, byte[] encodedTag, File file) {
		// TODO
	}

	private void removeOldTagAndWriteNewTag(int oldTagSize, byte[] encodedTag, File file) {
		// TODO
	}

	private void insertNewTag(byte[] encodedTag, File file) throws IOException {
		int paddingSize = calculatePaddingSize(encodedTag.length);
		File taggedFile = createTempFile("tagged-", ".mp3", file.getParentFile());
		try (FileOutputStream taggedFileOutputStream = new FileOutputStream(taggedFile)) {
			taggedFileOutputStream.write(encodedTag);
			taggedFileOutputStream.write(new byte[paddingSize - encodedTag.length]);
			copy(file, taggedFileOutputStream);
		} catch (IOException ioe1) {
			taggedFile.delete();
			throw ioe1;
		}
		if (!taggedFile.renameTo(file)) {
			taggedFile.delete();
			throw new IOException("could not rename file");
		}
	}

	private int calculatePaddingSize(int tagLength) {
		return 1024 * (((tagLength + 1536) / 1024) + 1);
	}

	private boolean fileIsAnMp3File(File file) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
			byte firstByte = randomAccessFile.readByte();
			if ((firstByte & 0xff) != 0xff) {
				return false;
			}
			byte secondByte = randomAccessFile.readByte();
			return (secondByte & 0xe0) == 0xe0;
		}
	}

}
