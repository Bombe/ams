package net.pterodactylus.util.tag.id3.v2_3;

import static com.google.common.io.ByteStreams.readFully;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility methods for handling ID3 v2.3 tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class ID3v23Utils {

	public static int read16Bits(InputStream inputStream) throws IOException {
		byte[] buffer = readBuffer(inputStream, new byte[2]);
		return ((buffer[0] & 0xff) << 8) | (buffer[1] & 0xff);
	}

	public static int read28Bits(InputStream inputStream) throws IOException {
		byte[] buffer = readBuffer(inputStream, new byte[4]);
		return (buffer[0] << 21) | (buffer[1] << 14) | (buffer[2] << 7) | buffer[3];
	}

	public static int read32Bits(InputStream inputStream) throws IOException {
		byte[] buffer = readBuffer(inputStream, new byte[4]);
		return (buffer[0] << 24) | ((buffer[1] & 0xff) << 16) | ((buffer[2] & 0xff) << 8) | (buffer[3] & 0xff);
	}

	public static byte readByte(InputStream inputStream) throws IOException {
		int r = inputStream.read();
		if (r == -1) {
			throw new EOFException();
		}
		return (byte) r;
	}

	public static byte[] readBuffer(InputStream inputStream, byte[] buffer) throws IOException {
		readFully(inputStream, buffer);
		return buffer;
	}

}
