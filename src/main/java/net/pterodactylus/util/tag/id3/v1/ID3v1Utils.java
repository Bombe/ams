package net.pterodactylus.util.tag.id3.v1;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * Utility methods for handling ID3v1 tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1Utils {

	static byte[] readBuffer(SeekableByteChannel seekableByteChannel) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(128);
		int remaining = 128;
		while (remaining > 0) {
			int r = seekableByteChannel.read(byteBuffer);
			if (r == -1) {
				throw new EOFException();
			}
			remaining -= r;
		}
		byteBuffer.flip();
		byte[] tagBuffer = new byte[128];
		byteBuffer.get(tagBuffer);
		return tagBuffer;
	}

}
