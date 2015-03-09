package net.pterodactylus.util.tag.id3.v2_3;

import static com.google.common.primitives.Ints.tryParse;
import static java.util.Arrays.copyOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static net.pterodactylus.util.tag.id3.v2_3.Counters.parseCounters;
import static net.pterodactylus.util.tag.id3.v2_3.Frame.parseFrame;
import static net.pterodactylus.util.tag.id3.v2_3.Header.parseHeader;
import static net.pterodactylus.util.tag.id3.v2_3.ID3v23Utils.readBuffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
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

	private final Unsynchronizer unsynchronizer = new Unsynchronizer();

	@Override
	public Optional<Tag> readTags(Path file) throws IOException {
		try (InputStream fileInputStream = Files.newInputStream(file)) {
			Optional<Header> header = parseHeader(fileInputStream);
			if (!header.isPresent()) {
				return empty();
			}
			if (header.get().getMajorVersion() != 3) {
				return empty();
			}
			byte[] completeHeader = readBuffer(fileInputStream, new byte[header.get().getSize()]);
			if (header.get().isUnsynchronized()) {
				completeHeader = unsynchronizer.deunsynchronize(completeHeader);
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
		} else if (frameIdentifier.equals("TPE2")) {
			tag.setAlbumArtist(frame.getDecodedText());
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
			LocalDate oldDate = tag.getDate().orElse(LocalDate.of(0, 1, 1));
			tag.setDate(LocalDate.of(of(frame.getDecodedText()).map(Ints::tryParse).orElse(0), oldDate.getMonthValue(), oldDate.getDayOfMonth()));
		} else if (frameIdentifier.equals("TDAT")) {
			String formattedDate = frame.getDecodedText();
			if (formattedDate.length() != 4) {
				return;
			}
			int day = ofNullable(tryParse(formattedDate.substring(0, 2))).orElse(0);
			int month = ofNullable(tryParse(formattedDate.substring(2, 4))).orElse(0);
			if ((day == 0) || (month == 0)) {
				return;
			}
			LocalDate oldDate = tag.getDate().orElse(LocalDate.of(0, 1, 1));
			tag.setDate(LocalDate.of(oldDate.getYear(), month, day));
		}
	}

	private Optional<Frame> readNextFrame(InputStream inputStream) {
		try {
			return parseFrame(inputStream);
		} catch (IOException ioe1) {
			return empty();
		}
	}

}
