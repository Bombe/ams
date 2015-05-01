package net.pterodactylus.util.tag.id3.v2_3;

import static com.google.common.primitives.Bytes.concat;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.util.Arrays.copyOfRange;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.bits.ByteArrayEncoders.integer16MsbFirstEncoder;
import static net.pterodactylus.util.bits.ByteArrayEncoders.integer32MsbFirstEncoder;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.read16Bits;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.read32Bits;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.readBuffer;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.readByte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

import net.pterodactylus.util.bits.ByteArrayDecoders;

import com.google.common.primitives.Bytes;

/**
 * An ID3 v2.3 frame.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class Frame {

	private enum Flags {

		DISCARD_ON_ALTERED_TAG(0x8000),
		DISCARD_ON_ALTERED_FILE(0x4000),
		READ_ONLY(0x2000),
		COMPRESSED(0x80),
		ENCRYPTED(0x40),
		GROUPED(0x20);

		private final int bitValue;

		Flags(int bitValue) {
			this.bitValue = bitValue;
		}

		public int setOn(int flags) {
			return flags | bitValue;
		}

		public boolean isSet(int flags) {
			return (flags & bitValue) != 0;
		}

	}

	private enum StringEncoding {

		LATIN_1(0),
		UCS_2(1);

		private final int encoding;

		StringEncoding(int encoding) {
			this.encoding = encoding;
		}

		public int encoding() {
			return encoding;
		}

		public byte[] prependEncoding(byte[] textBytes) {
			return Bytes.concat(new byte[] { (byte) encoding }, textBytes);
		}

	}

	private static final Pattern FRAME_ID_PATTERN = Pattern.compile("[A-Z0-9]{4}");

	private final String frameId;
	private final int flags;
	private final byte[] data;
	private final Optional<Integer> decompressedSize;
	private final Optional<Byte> enryptionMethod;
	private final Optional<Byte> groupingIdentity;

	private Frame(String frameId, int flags, byte[] data, Optional<Integer> decompressedSize,
			Optional<Byte> enryptionMethod, Optional<Byte> groupingIdentity) {
		if (!FRAME_ID_PATTERN.matcher(frameId).matches()) {
			throw new InvalidFrameName();
		}
		this.frameId = frameId;
		this.flags = flags;
		this.data = data;
		this.decompressedSize = decompressedSize;
		this.enryptionMethod = enryptionMethod;
		this.groupingIdentity = groupingIdentity;
	}

	public String getFrameId() {
		return frameId;
	}

	public int getSize() {
		return data.length;
	}

	public String getDecodedText() {
		byte stringEncoding = data[0];
		if (stringEncoding == StringEncoding.LATIN_1.encoding()) {
			return decodeAsLatin1(copyOfRange(data, 1, data.length));
		} else if (stringEncoding == StringEncoding.UCS_2.encoding()) {
			return decodeAsUTF16(copyOfRange(data, 1, data.length));
		}
		throw new InvalidStringEncoding(stringEncoding);
	}

	public byte[] encode() {
		try (ByteArrayOutputStream renderedFrame = new ByteArrayOutputStream()) {
			renderedFrame.write(frameId.getBytes(ISO_8859_1));
			renderedFrame.write(integer32MsbFirstEncoder().apply(data.length));
			renderedFrame.write(integer16MsbFirstEncoder().apply(flags));
			if (Flags.COMPRESSED.isSet(flags)) {
				renderedFrame.write(integer32MsbFirstEncoder().apply(decompressedSize.get()));
			}
			if (Flags.ENCRYPTED.isSet(flags)) {
				renderedFrame.write(enryptionMethod.get());
			}
			if (Flags.GROUPED.isSet(flags)) {
				renderedFrame.write(groupingIdentity.get());
			}
			renderedFrame.write(data);
			return renderedFrame.toByteArray();
		} catch (IOException ioe1) {
			/* ByteArrayOutputStream doesn’t do exceptions. */
			throw new RuntimeException(ioe1);
		}
	}

	public static Frame createTextFrame(String name, String value) throws InvalidFrameName, NotATextFrame {
		if (!name.startsWith("T") || name.equals("TXXX")) {
			throw new NotATextFrame();
		}
		if (stringIsEncodableAsLatin1(value)) {
			byte[] encodedString = encodeAsLatin1(value);
			return new TextFrameBuilder(name).withData(encodedString).build();
		}
		byte[] unicodeString = encodeAsUnicode(value);
		return new TextFrameBuilder(name).withData(unicodeString).build();
	}

	public static class InvalidStringEncoding extends RuntimeException {

		public InvalidStringEncoding(byte stringEncoding) {
			super(format("Invalid string encoding: %d", stringEncoding));
		}

	}

	public static class NotATextFrame extends RuntimeException { }

	public static class InvalidFrameName extends RuntimeException { }

	private String decodeAsUTF16(byte[] buffer) {
		return new String(buffer, UTF_16);
	}

	private String decodeAsLatin1(byte[] buffer) {
		return new String(buffer, ISO_8859_1);
	}

	private static boolean stringIsEncodableAsLatin1(String value) {
		return StandardCharsets.ISO_8859_1.newEncoder()
				.onUnmappableCharacter(CodingErrorAction.REPORT)
				.canEncode(value);
	}

	private static byte[] encodeAsLatin1(String value) {
		return encodeString(value, StandardCharsets.ISO_8859_1, StringEncoding.LATIN_1);
	}

	private static byte[] encodeAsUnicode(String value) {
		return encodeString(value, StandardCharsets.UTF_16, StringEncoding.UCS_2);
	}

	private static byte[] encodeString(String value, Charset charset, StringEncoding stringEncoding) {
		byte[] textBytes = value.getBytes(charset);
		return stringEncoding.prependEncoding(textBytes);
	}

	public static Optional<Frame> parseFrame(InputStream inputStream) throws IOException {
		byte[] firstByte = new byte[] { readByte(inputStream) };
		if (firstByte[0] == 0) {
			return empty();
		}
		byte[] idBuffer = readBuffer(inputStream, new byte[3]);
		int size = read32Bits(inputStream);
		int flags = read16Bits(inputStream);
		byte[] data = readBuffer(inputStream, new byte[size]);
		Optional<Integer> decompressedSize = empty();
		int dataBytesToSkip = 0;
		if (Flags.COMPRESSED.isSet(flags)) {
			decompressedSize = of(ByteArrayDecoders.integer32BitMsbFirstDecoder().apply(data));
			dataBytesToSkip += 4;
		}
		Optional<Byte> encryptionType = empty();
		if (Flags.ENCRYPTED.isSet(flags)) {
			encryptionType = of(data[dataBytesToSkip]);
			dataBytesToSkip++;
		}
		Optional<Byte> groupingIdentity = empty();
		if (Flags.GROUPED.isSet(flags)) {
			groupingIdentity = of(data[dataBytesToSkip]);
			dataBytesToSkip++;
		}
		return of(new Frame(createString(concat(firstByte, idBuffer)), flags,
				copyOfRange(data, dataBytesToSkip, data.length - dataBytesToSkip), decompressedSize, encryptionType,
				groupingIdentity));
	}

	private static String createString(byte[] buffer) {
		StringBuilder stringBuilder = new StringBuilder(buffer.length);
		for (byte b : buffer) {
			stringBuilder.append((char) (b & 0xff));
		}
		return stringBuilder.toString();
	}

	public static class TextFrameBuilder {

		private final String frameId;
		private int flags;
		private int decompressedSize;
		private byte encryptionMethod;
		private byte groupIdentifier;
		private byte[] data;

		public TextFrameBuilder(String frameId) throws InvalidFrameName {
			if (!FRAME_ID_PATTERN.matcher(frameId).matches()) {
				throw new InvalidFrameName();
			}
			this.frameId = frameId;
		}

		public TextFrameBuilder discardWhenTagAltered() {
			flags |= Flags.DISCARD_ON_ALTERED_TAG.setOn(flags);
			return this;
		}

		public TextFrameBuilder discardWhenFileAltered() {
			flags |= Flags.DISCARD_ON_ALTERED_FILE.setOn(flags);
			return this;
		}

		public TextFrameBuilder readOnly(byte encryptionMethod) {
			flags |= Flags.READ_ONLY.setOn(flags);
			this.encryptionMethod = encryptionMethod;
			return this;
		}

		public TextFrameBuilder compressed(int decompressedSize) {
			flags |= Flags.COMPRESSED.setOn(flags);
			this.decompressedSize = decompressedSize;
			return this;
		}

		public TextFrameBuilder encrypted() {
			flags |= Flags.ENCRYPTED.setOn(flags);
			return this;
		}

		public TextFrameBuilder grouped(byte groupIdentifier) {
			flags |= Flags.GROUPED.setOn(flags);
			this.groupIdentifier = groupIdentifier;
			return this;
		}

		public TextFrameBuilder withData(byte[] data) {
			this.data = data;
			return this;
		}

		public Frame build() {
			return new Frame(frameId, flags, data,
					Flags.COMPRESSED.isSet(flags) ? Optional.of(decompressedSize) : empty(),
					Flags.ENCRYPTED.isSet(flags) ? Optional.of(encryptionMethod) : empty(),
					Flags.GROUPED.isSet(flags) ? Optional.of(groupIdentifier) : empty()
			);
		}

	}

}
