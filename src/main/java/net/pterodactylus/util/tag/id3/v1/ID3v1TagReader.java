package net.pterodactylus.util.tag.id3.v1;

import static java.util.Optional.empty;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
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
	public Optional<Tag> readTags(Path file) throws IOException {
		if (Files.size(file) < 128) {
			return empty();
		}
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(file)) {
			byte[] tagBuffer = readTag(seekableByteChannel);
			Optional<Tag> id3v1Tag = tagDecoder.parse(tagBuffer);
			return id3v1Tag;
		}
	}

	private byte[] readTag(SeekableByteChannel seekableByteChannel) throws IOException {
		seekableByteChannel.position(seekableByteChannel.size() - 128);
		ByteBuffer byteBuffer = ByteBuffer.allocate(128);
		seekableByteChannel.read(byteBuffer);
		byteBuffer.flip();
		byte[] tagBuffer = new byte[128];
		byteBuffer.get(tagBuffer);
		return tagBuffer;
	}

}
