package net.pterodactylus.util.tag.id3.v1;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.Charset.forName;
import static java.nio.charset.CodingErrorAction.REPORT;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.v1.Genre.getNumber;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.time.LocalDate;
import java.util.Optional;

import net.pterodactylus.util.StringUtils;
import net.pterodactylus.util.tag.AbstractTag;
import net.pterodactylus.util.tag.Tag;

/**
 * ID3v1 tag.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1Tag extends AbstractTag {

	private static final int MAX_NAME_LENGTH = 30;
	private static final int MAX_ARTIST_LENGTH = 30;
	private static final int MAX_ALBUM_LENGTH = 30;
	private static final int MAX_TRACK_NUMBER = 99;
	private static final int MAX_COMMENT_LENGTH = 30;
	private static final int REDUCED_MAX_COMMENT_LENGTH = 28;

	@Override
	public boolean isEncodable() {
		if (getName().isPresent() && (getName().get().length() > MAX_NAME_LENGTH)) {
			return false;
		}
		if (getArtist().isPresent() && (getArtist().get().length() > MAX_ARTIST_LENGTH)) {
			return false;
		}
		if (getAlbumArtist().isPresent()) {
			return false;
		}
		if (getAlbum().isPresent() && (getAlbum().get().length() > MAX_ALBUM_LENGTH)) {
			return false;
		}
		if (getTrack().isPresent() && ((getTrack().get() < 0) || (getTrack().get() > MAX_TRACK_NUMBER))) {
			return false;
		}
		if (getTotalTracks().isPresent()) {
			return false;
		}
		if (getDisc().isPresent()) {
			return false;
		}
		if (getTotalDiscs().isPresent()) {
			return false;
		}
		if (getGenre().isPresent() && !getNumber(getGenre().get()).isPresent()) {
			return false;
		}
		if (getComment().isPresent()) {
			if (getComment().get().length() > MAX_COMMENT_LENGTH) {
				return false;
			}
			if (getTrack().isPresent() && (getComment().get().length() > REDUCED_MAX_COMMENT_LENGTH)) {
				return false;
			}
		}
		return true;
	}

	public static Optional<Tag> parse(byte[] tagData) {
		checkArgument(tagData.length == 128);
		if ((tagData[0] != 'T') || (tagData[1] != 'A') || (tagData[2] != 'G')) {
			return empty();
		}
		Optional<String> title = decodeString(tagData, 3, 30);
		Optional<String> artist = decodeString(tagData, 33, 30);
		Optional<String> album = decodeString(tagData, 63, 30);
		Optional<Integer> year = decodeInteger(tagData, 93, 4);
		Optional<String> comment;
		Optional<Integer> track = empty();
		if (tagData[125] == 0) {
			comment = decodeString(tagData, 97, 28);
			track = (tagData[126] != 0) ? of((int) tagData[126]) : empty();
		} else {
			comment = decodeString(tagData, 97, 30);
		}
		Optional<Integer> genre = ((tagData[127] & 0xff) != 0xff) ? of(tagData[127] & 0xff) : empty();
		return of(new ID3v1Tag().setName(title.orElse(null)).setArtist(artist.orElse(null)).setAlbum(album.orElse(null)).setDate(year.map(y -> LocalDate.ofYearDay(y, 1)).orElse(null)).setComment(comment.orElse(null)).setTrack(track.orElse(0)).setGenre(genre.flatMap(Genre::getName).orElse(null)));
	}

	private static Optional<Integer> decodeInteger(byte[] buffer, int offset, int length) {
		try {
			return decodeString(buffer, offset, length).map(Integer::parseInt);
		} catch (NumberFormatException nfe1) {
			return empty();
		}
	}

	private static Optional<String> decodeString(byte[] buffer, int offset, int length) {
		Optional<String> decodedString = tryDecoding("UTF-8", buffer, offset, length);
		if (decodedString.isPresent()) {
			return StringUtils.normalize(decodedString.get());
		}
		decodedString = tryDecoding("ISO8859-15", buffer, offset, length);
		if (decodedString.isPresent()) {
			return StringUtils.normalize(decodedString.get());
		}
		return empty();
	}

	private static Optional<String> tryDecoding(String charsetName, byte[] buffer, int offset, int length) {
		CharsetDecoder charsetDecoder = getCharsetDecoder(charsetName);
		try {
			CharBuffer decodedBuffer = charsetDecoder.decode(wrap(buffer, offset, length));
			return of(new String(decodedBuffer.array()));
		} catch (CharacterCodingException cce1) {
			return empty();
		}
	}

	private static CharsetDecoder getCharsetDecoder(String charsetName) {
		Charset charset = forName(charsetName);
		CharsetDecoder charsetDecoder = charset.newDecoder();
		charsetDecoder.onMalformedInput(REPORT);
		return charsetDecoder;
	}

}
