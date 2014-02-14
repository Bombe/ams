package net.pterodactylus.util.tag.id3.v1;

import static java.util.Optional.empty;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Utils.readBuffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagReader;

/**
 * Reads an ID3v1 {@link Tag} from a {@link File}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagReader implements TagReader {

	private static final ID3v1TagDecoder tagDecoder = new ID3v1TagDecoder();

	@Override
	public Optional<Tag> readTags(File file) throws IOException {
		if (file.length() < 128) {
			return empty();
		}
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
			byte[] tagBuffer = readTag(randomAccessFile);
			Optional<Tag> id3v1Tag = tagDecoder.parse(tagBuffer);
			return id3v1Tag;
		}
	}

	private byte[] readTag(RandomAccessFile randomAccessFile) throws IOException {
		randomAccessFile.seek(randomAccessFile.length() - 128);
		byte[] tagBuffer = readBuffer(randomAccessFile);
		return tagBuffer;
	}

}
