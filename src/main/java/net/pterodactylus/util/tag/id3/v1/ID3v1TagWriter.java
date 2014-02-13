package net.pterodactylus.util.tag.id3.v1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Writes ID3v1 tags to the appropriate places in a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class ID3v1TagWriter {

	private static final ID3v1TagReader tagReader = new ID3v1TagReader();
	private static final ID3v1TagEncoder tagEncoder = new ID3v1TagEncoder();

	public boolean writeTag(File file, ID3v1Tag tag) throws IOException {
		if (tagReader.readTags(file).isPresent()) {
			overwriteOldTag(file, tag);
		}
		appendTag(file, tag);
		return false;
	}

	private void overwriteOldTag(File file, ID3v1Tag tag) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
			randomAccessFile.seek(randomAccessFile.length() - 128);
			writeTagToFile(randomAccessFile, tag);
		}
	}

	private void appendTag(File file, ID3v1Tag tag) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
			randomAccessFile.seek(randomAccessFile.length());
			writeTagToFile(randomAccessFile, tag);
		}
	}

	private void writeTagToFile(RandomAccessFile randomAccessFile, ID3v1Tag tag) throws IOException {
		randomAccessFile.write(tagEncoder.encode(tag));
	}

}
