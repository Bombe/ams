package net.pterodactylus.util.tag.id3.v1;

import static java.util.Optional.empty;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagReader;

/**
 * Reads an {@link ID3v1Tag} from a {@link File}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagReader implements TagReader {

	@Override
	public Optional<Tag> readTags(File file) throws IOException {
		if (file.length() < 128) {
			return empty();
		}
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
			byte[] tagBuffer = readTag(randomAccessFile);
			Optional<Tag> id3v1Tag = ID3v1Tag.parse(tagBuffer);
			return id3v1Tag;
		}
	}

	private byte[] readTag(RandomAccessFile randomAccessFile) throws IOException {
		randomAccessFile.seek(randomAccessFile.length() - 128);
		byte[] tagBuffer = readBuffer(randomAccessFile);
		return tagBuffer;
	}

	private byte[] readBuffer(RandomAccessFile randomAccessFile) throws IOException {
		byte[] tagBuffer = new byte[128];
		int remaining = 128;
		while (remaining > 0) {
			int r = randomAccessFile.read(tagBuffer, 128 - remaining, remaining);
			if (r == -1) {
				throw new EOFException();
			}
			remaining -= r;
		}
		return tagBuffer;
	}

}
