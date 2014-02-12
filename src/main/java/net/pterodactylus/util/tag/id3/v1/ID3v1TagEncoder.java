package net.pterodactylus.util.tag.id3.v1;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.min;
import static java.lang.String.valueOf;
import static java.lang.System.arraycopy;
import static java.nio.charset.Charset.forName;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.ALBUM_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.ARTIST_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.COMMENT_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.GENRE_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.TITLE_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.TRACK_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.YEAR_OFFSET;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;

/**
 * Encodes a {@link Tag} into an ID3v1 tag.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagEncoder {

	private static final Charset utf8Charset = forName("UTF-8");

	public byte[] encode(Tag tag) {
		checkNotNull(tag, "tag must not be null");
		byte[] tagBuffer = new byte[128];
		storeString(tagBuffer, 0, 3, "TAG");
		storeString(tagBuffer, TITLE_OFFSET, 30, tag.getName().orElse(""));
		storeString(tagBuffer, ARTIST_OFFSET, 30, tag.getArtist().orElse(""));
		storeString(tagBuffer, ALBUM_OFFSET, 30, tag.getAlbum().orElse(""));
		storeString(tagBuffer, YEAR_OFFSET, 4, encodeDate(tag.getDate()));
		storeString(tagBuffer, COMMENT_OFFSET, 30, tag.getComment().orElse(""));
		if (tag.getTrack().isPresent()) {
			tagBuffer[TRACK_OFFSET - 1] = 0;
			tagBuffer[TRACK_OFFSET] = tag.getTrack().get().byteValue();
		}
		tagBuffer[GENRE_OFFSET] = tag.getGenre().flatMap(Genre::getNumber).orElse(255).byteValue();
		return tagBuffer;
	}

	private String encodeDate(Optional<LocalDate> date) {
		return !date.isPresent() ? "" : valueOf(date.get().getYear());
	}

	private void storeString(byte[] tagBuffer, int offset, int length, String text) {
		byte[] textBytes = text.getBytes(utf8Charset);
		int bytesToCopy = min(length, textBytes.length);
		int bytesToFill = length - bytesToCopy;
		arraycopy(textBytes, 0, tagBuffer, offset, bytesToCopy);
		arraycopy(new byte[bytesToFill], 0, tagBuffer, offset + bytesToCopy, bytesToFill);
	}

}
