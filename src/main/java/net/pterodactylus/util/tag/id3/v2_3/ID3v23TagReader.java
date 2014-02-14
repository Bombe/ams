package net.pterodactylus.util.tag.id3.v2_3;

import static java.time.LocalDate.ofYearDay;
import static java.util.Arrays.copyOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.v2_3.Counters.parseCounters;
import static net.pterodactylus.util.tag.id3.v2_3.Frame.parseFrame;
import static net.pterodactylus.util.tag.id3.v2_3.Header.parseHeader;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.readBuffer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagReader;

import com.google.common.primitives.Ints;

/**
 * {@link TagReader} implementation that can parse ID3 v2.3 tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v23TagReader implements TagReader {

	@Override
	public Optional<Tag> readTags(File file) throws IOException {
		try (InputStream fileInputStream = new FileInputStream(file)) {
			Optional<Header> header = parseHeader(fileInputStream);
			if (!header.isPresent()) {
				return empty();
			}
			if (header.get().getMajorVersion() != 3) {
				return empty();
			}
			byte[] completeHeader = readBuffer(fileInputStream, new byte[header.get().getSize()]);
			if (header.get().isUnsynchronized()) {
				completeHeader = deunsynchronize(completeHeader);
			}
			if (header.get().isExtendedHeaderFollows()) {
				ExtendedHeader extendedHeader = ExtendedHeader.parse(completeHeader);
				if (extendedHeader.getPadding() > 0) {
					completeHeader = copyOf(completeHeader, completeHeader.length - extendedHeader.getPadding());
				}
			}
			Tag tag = new Tag();
			try (InputStream headerInputStream = new ByteArrayInputStream(completeHeader)) {
				Optional<Frame> frame;
				while ((frame = readNextFrame(headerInputStream)).isPresent()) {
					processFrame(frame.get(), tag);
				}
			}
			return of(tag);
		}
	}

	private void processFrame(Frame frame, Tag tag) {
		String frameIdentifier = frame.getIdentifier();
		if (frameIdentifier.equals("TPE1")) {
			tag.setArtist(frame.getDecodedText());
		} else if (frameIdentifier.equals("TALB")) {
			tag.setAlbum(frame.getDecodedText());
		} else if (frameIdentifier.equals("TIT2")) {
			tag.setName(frame.getDecodedText());
		} else if (frameIdentifier.equals("TPOS")) {
			Optional<Counters> counters = parseCounters(frame.getDecodedText());
			if (counters.isPresent()) {
				tag.setDisc(counters.get().getCurrent());
				tag.setTotalDiscs(counters.get().getTotal());
			}
		} else if (frameIdentifier.equals("TRCK")) {
			Optional<Counters> counters = parseCounters(frame.getDecodedText());
			if (counters.isPresent()) {
				tag.setTrack(counters.get().getCurrent());
				tag.setTotalTracks(counters.get().getTotal());
			}
		} else if (frameIdentifier.equals("TCON")) {
			tag.setGenre(frame.getDecodedText());
		} else if (frameIdentifier.equals("TYER")) {
			tag.setDate(of(frame.getDecodedText()).map(Ints::tryParse).map(year -> ofYearDay(year, 1)).orElse(null));
		}
	}

	private Optional<Frame> readNextFrame(InputStream inputStream) {
		try {
			return parseFrame(inputStream);
		} catch (IOException ioe1) {
			return empty();
		}
	}

	private byte[] deunsynchronize(byte[] completeHeader) {
		byte[] deunsynchronizedHeader = new byte[completeHeader.length];
		boolean lastWas0xff = false;
		int currentIndex = 0;
		for (byte b : completeHeader) {
			if (lastWas0xff) {
				lastWas0xff = false;
				if (b == 0) {
					continue;
				}
			} else if (b == (byte) 0xff) {
				lastWas0xff = true;
			}
			deunsynchronizedHeader[currentIndex++] = b;
		}
		return copyOf(deunsynchronizedHeader, currentIndex);
	}

}
