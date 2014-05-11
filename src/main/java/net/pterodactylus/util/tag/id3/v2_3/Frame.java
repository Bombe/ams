package net.pterodactylus.util.tag.id3.v2_3;

import static com.google.common.primitives.Bytes.concat;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.util.Arrays.copyOfRange;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.read16Bits;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.read32Bits;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.readBuffer;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.readByte;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * An ID3 v2.3 frame.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class Frame {

	private static final int STRING_ENCODING_LATIN1 = 0;
	private static final int STRING_ENCODING_UTF16 = 1;

	private final String identifier;
	private final int size;
	private final int flags;
	private final byte[] data;

	private Frame(String identifier, int size, int flags, byte[] data, Optional<byte[]> decompressedSize, Optional<Byte> enryptionMethod, Optional<Byte> groupingIdentity) {
		this.identifier = identifier;
		this.size = size;
		this.flags = flags;
		this.data = data;
	}

	public String getIdentifier() {
		return identifier;
	}

	public int getSize() {
		return size;
	}

	public boolean isFlagSet(int position) {
		if ((position < 0) || (position > 15)) {
			return false;
		}
		return (flags & (1 << (15 - position))) != 0;
	}

	public String getDecodedText() {
		byte stringEncoding = data[0];
		if (stringEncoding == STRING_ENCODING_LATIN1) {
			return decodeAsLatin1(copyOfRange(data, 1, data.length));
		} else if (stringEncoding == STRING_ENCODING_UTF16) {
			return decodeAsUTF16(copyOfRange(data, 1, data.length));
		}
		throw new RuntimeException(format("Invalid string encoding: %d", stringEncoding));
	}

	private String decodeAsUTF16(byte[] buffer) {
		return new String(buffer, UTF_16);
	}

	private String decodeAsLatin1(byte[] buffer) {
		return new String(buffer, ISO_8859_1);
	}

	public static Optional<Frame> parseFrame(InputStream inputStream) throws IOException {
		byte[] firstByte = new byte[]{readByte(inputStream)};
		if (firstByte[0] == 0) {
			return empty();
		}
		byte[] idBuffer = readBuffer(inputStream, new byte[3]);
		int size = read32Bits(inputStream);
		int flags = read16Bits(inputStream);
		byte[] data = readBuffer(inputStream, new byte[size]);
		Optional<byte[]> decompressedSize = empty();
		int dataBytesToSkip = 0;
		if ((flags & 0b10000000) != 0) {
			decompressedSize = of(copyOfRange(data, dataBytesToSkip, dataBytesToSkip + 4));
			dataBytesToSkip += 4;
		}
		Optional<Byte> encryptionType = empty();
		if ((flags & 0b01000000) != 0) {
			encryptionType = of(data[dataBytesToSkip]);
			dataBytesToSkip++;
		}
		Optional<Byte> groupingIdentity = empty();
		if ((flags & 0b00100000) != 0) {
			groupingIdentity = of(data[dataBytesToSkip]);
			dataBytesToSkip++;
		}
		return of(new Frame(createString(concat(firstByte, idBuffer)), size, flags, copyOfRange(data, dataBytesToSkip, data.length - dataBytesToSkip), decompressedSize, encryptionType, groupingIdentity));
	}

	private static String createString(byte[] buffer) {
		StringBuilder stringBuilder = new StringBuilder(buffer.length);
		for (byte b : buffer) {
			stringBuilder.append((char) (b & 0xff));
		}
		return stringBuilder.toString();
	}

}
