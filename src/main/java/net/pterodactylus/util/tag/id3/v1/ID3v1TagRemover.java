package net.pterodactylus.util.tag.id3.v1;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import net.pterodactylus.util.tag.TagRemover;

/**
 * Removes an ID3v1 tag from a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagRemover implements TagRemover {

	private static final ID3v1TagReader tagReader = new ID3v1TagReader();

	@Override
	public boolean removeTag(Path file) throws IOException {
		if (tagReader.readTags(file).isPresent()) {
			removeTagFromFile(file);
			return true;
		}
		return false;
	}

	private void removeTagFromFile(Path file) throws IOException {
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(file, StandardOpenOption.WRITE)) {
			seekableByteChannel.truncate(seekableByteChannel.size() - 128);
		}
	}

}
