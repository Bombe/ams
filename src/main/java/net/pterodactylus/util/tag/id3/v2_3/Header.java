package net.pterodactylus.util.tag.id3.v2_3;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.read28Bits;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.readByte;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * An ID3 v2.3 header.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class Header {

	public static final int UNSYNCHRONIZATION_FLAG = 0b10000000;
	public static final int EXTENDED_HEADER_FLAG = 0b01000000;
	public static final int EXPERIMENTAL_FLAG = 0b00100000;

	private final int majorVersion;
	private final int revision;
	private final boolean unsynchronized;
	private final boolean extendedHeaderFollows;
	private final boolean experimental;
	private final int size;

	private Header(int majorVersion, int revision, boolean unsynchronized, boolean extendedHeaderFollows, boolean experimental, int size) {
		this.majorVersion = majorVersion;
		this.revision = revision;
		this.unsynchronized = unsynchronized;
		this.extendedHeaderFollows = extendedHeaderFollows;
		this.experimental = experimental;
		this.size = size;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getRevision() {
		return revision;
	}

	public boolean isUnsynchronized() {
		return unsynchronized;
	}

	public boolean isExtendedHeaderFollows() {
		return extendedHeaderFollows;
	}

	public boolean isExperimental() {
		return experimental;
	}

	public int getSize() {
		return size;
	}

	public static Optional<Header> parseHeader(InputStream inputStream) throws IOException {
		byte[] header = ID3v23Utils.readBuffer(inputStream, new byte[3]);
		if ((header[0] != 'I') || (header[1] != 'D') || (header[2] != '3')) {
			return empty();
		}
		int majorVersion = readByte(inputStream);
		int revision = readByte(inputStream);
		int flags = readByte(inputStream);
		int size = read28Bits(inputStream);
		boolean unsychronized = (flags & UNSYNCHRONIZATION_FLAG) != 0;
		boolean extendedHeaderFollows = (flags & EXTENDED_HEADER_FLAG) != 0;
		boolean experimental = (flags & EXPERIMENTAL_FLAG) != 0;
		return of(new Header(majorVersion, revision, unsychronized, extendedHeaderFollows, experimental, size));
	}

}
