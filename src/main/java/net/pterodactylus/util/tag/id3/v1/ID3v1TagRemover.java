package net.pterodactylus.util.tag.id3.v1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import net.pterodactylus.util.tag.TagRemover;

/**
 * Removes an ID3v1 tag from a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagRemover implements TagRemover {

	private static final ID3v1TagReader tagReader = new ID3v1TagReader();

	@Override
	public boolean removeTag(File file) throws IOException {
		if (tagReader.readTags(file).isPresent()) {
			removeTagFromFile(file);
			return true;
		}
		return false;
	}

	private void removeTagFromFile(File file) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
			randomAccessFile.getChannel().truncate(randomAccessFile.length() - 128);
		}
	}

}
