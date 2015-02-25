package net.pterodactylus.util.tag.id3.v2_3;

import static java.lang.String.format;
import static java.lang.System.arraycopy;
import static java.nio.CharBuffer.wrap;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static net.pterodactylus.util.tag.id3.v2_3.Header.UNSYNCHRONIZATION_FLAG;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.encode16Bits;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.encode28Bits;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.encode32Bits;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.read28Bits;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;

/**
 * Encoder for an ID3v2 version 2.3 tag.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TagEncoder {

	private static final CharsetEncoder latin1Encoder = ISO_8859_1.newEncoder();
	private static final CharsetEncoder utf16Encoder = UTF_16BE.newEncoder();
	private static final Unsynchronizer unsynchronizer = new Unsynchronizer();

	public byte[] encode(Tag tag, Optional<Integer> paddingSize) {
		byte[] id3Header = createId3Tag(tag);
		return headerRequiresUnsynchronisation(id3Header) ? unsynchronize(id3Header) : id3Header;
	}

	private byte[] createId3Tag(Tag tag) {
		try (ByteArrayOutputStream frameStream = new ByteArrayOutputStream()) {
			if (tag.getArtist().isPresent()) {
				frameStream.write(createTextFrame("TPE1", tag.getArtist().get()));
			}
			if (tag.getName().isPresent()) {
				frameStream.write(createTextFrame("TIT2", tag.getName().get()));
			}
			if (tag.getAlbum().isPresent()) {
				frameStream.write(createTextFrame("TALB", tag.getAlbum().get()));
			}
			if (tag.getAlbumArtist().isPresent()) {
				frameStream.write(createTextFrame("TPE2", tag.getAlbumArtist().get()));
			}
			if (tag.getTrack().isPresent() || tag.getTotalTracks().isPresent()) {
				frameStream.write(createTextFrame("TRCK", new Counters(tag.getTrack().orElse(0), tag.getTotalTracks().orElse(0)).toString()));
			}
			if (tag.getDisc().isPresent() || tag.getTotalDiscs().isPresent()) {
				frameStream.write(createTextFrame("TPOS", new Counters(tag.getDisc().orElse(0), tag.getTotalDiscs().orElse(0)).toString()));
			}
			if (tag.getDate().isPresent()) {
				frameStream.write(createTextFrame("TYER", format("%tY", tag.getDate().get())));
				frameStream.write(createTextFrame("TDAT", format("%1$td%1$tm", tag.getDate().get())));
			}
			return createTag(frameStream.toByteArray());
		} catch (IOException ioe1) {
			/* this should never happen. */
			throw new RuntimeException("I/O error while writing to memory buffer");
		}
	}

	private byte[] createTag(byte[] frames) throws IOException {
		try (ByteArrayOutputStream headerStream = new ByteArrayOutputStream()) {
			headerStream.write(new byte[] { 'I', 'D', '3' });
			headerStream.write(new byte[] { 0x03, 0x00 });
			headerStream.write(0x00);
			headerStream.write(encode28Bits(frames.length));
			headerStream.write(frames);
			return headerStream.toByteArray();
		}
	}

	private byte[] createTextFrame(String frameType, String text) throws IOException {
		try (ByteArrayOutputStream frame = new ByteArrayOutputStream()) {
			if (latin1Encoder.canEncode(text)) {
				frame.write(0x00);
				frame.write(latin1Encoder.encode(wrap(text)).array());
			} else {
				frame.write(0x01);
				frame.write(0xfe);
				frame.write(0xff);
				frame.write(utf16Encoder.encode(wrap(text)).array());
			}
			return createFrame(frameType, frame.toByteArray());
		}
	}

	private byte[] createFrame(String frameType, byte[] framePayload) throws IOException {
		if (framePayload.length == 0) {
			return new byte[0];
		}
		try (ByteArrayOutputStream frame = new ByteArrayOutputStream()) {
			frame.write(frameType.getBytes(US_ASCII));
			frame.write(encode32Bits(framePayload.length));
			frame.write(encode16Bits(0));
			frame.write(framePayload);
			return frame.toByteArray();
		}
	}

	private boolean headerRequiresUnsynchronisation(byte[] id3Header) {
		return unsynchronizer.isUnsychronizationRequired(id3Header);
	}

	private byte[] unsynchronize(byte[] id3Header) {
		byte[] unsynchronizedHeader = unsynchronizer.unsynchronize(id3Header);
		unsynchronizedHeader[5] |= UNSYNCHRONIZATION_FLAG;
		int newHeaderSize = unsynchronizedHeader.length;
		arraycopy(encode28Bits(newHeaderSize), 0, unsynchronizedHeader, 6, 4);
		return unsynchronizedHeader;
	}

}
