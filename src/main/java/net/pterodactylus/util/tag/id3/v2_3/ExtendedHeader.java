package net.pterodactylus.util.tag.id3.v2_3;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Arrays;
import java.util.Optional;

/**
 * An extended ID3 v2.3 header. It contains information about the padding and
 * optional CRC data.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class ExtendedHeader {

	private final int size;
	private final boolean crcDataPresent;
	private final int padding;
	private final Optional<byte[]> crc;

	private ExtendedHeader(int size, boolean crcDataPresent, int padding, Optional<byte[]> crc) {
		this.size = size;
		this.crcDataPresent = crcDataPresent;
		this.padding = padding;
		this.crc = crc;
	}

	public int getSize() {
		return size;
	}

	public boolean isCrcDataPresent() {
		return crcDataPresent;
	}

	public int getPadding() {
		return padding;
	}

	public Optional<byte[]> getCrc() {
		return crc;
	}

	public static ExtendedHeader parse(byte[] buffer) {
		int size = read(buffer, 0, 4);
		int flags = read(buffer, 4, 2);
		int padding = read(buffer, 6, 4);
		boolean crcDataPresent = (flags & 0b10000000_00000000) != 0;
		Optional<byte[]> crc = crcDataPresent ? of(Arrays.copyOfRange(buffer, 10, 14)) : empty();
		return new ExtendedHeader(size, crcDataPresent, padding, crc);
	}

	private static int read(byte[] buffer, int offset, int length) {
		int value = 0;
		for (int index = 0; index < length; ++index) {
			value = (value << 8) | (buffer[offset + index] & 0xff);
		}
		return value;
	}

}
