package net.pterodactylus.util.tag.id3.v1;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Utility methods for handling ID3v1 tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1Utils {

	static byte[] readBuffer(RandomAccessFile randomAccessFile) throws IOException {
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
