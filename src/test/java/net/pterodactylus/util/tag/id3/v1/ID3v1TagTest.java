package net.pterodactylus.util.tag.id3.v1;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.lang.System.arraycopy;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.v1.Genre.getName;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.ALBUM_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.ARTIST_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.COMMENT_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.GENRE_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.TITLE_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.TRACK_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.YEAR_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Tag.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;

import org.junit.Test;

/**
 * Unit test for {@link ID3v1Tag}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagTest {

	private final Tag tag = new ID3v1Tag();

	@Test
	public void titleIsDecodedCorrectlyAsUtf8() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(TITLE_OFFSET, 30, "ÜTF-8 Titlé ", "UTF-8");
		assertThat(id3v1Tag.get().getName(), is(of("ÜTF-8 Titlé")));
	}

	@Test
	public void titleIsDecodedCorrectlyAsLatin9() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(TITLE_OFFSET, 30, "ÜTF-8 Titlé ", "ISO8859-15");
		assertThat(id3v1Tag.get().getName(), is(of("ÜTF-8 Titlé")));
	}

	@Test
	public void artistIsDecodedCorrectlyAsUtf8() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(ARTIST_OFFSET, 30, "Ümlaut Ärtíst! ", "UTF-8");
		assertThat(id3v1Tag.get().getArtist(), is(of("Ümlaut Ärtíst!")));
	}

	@Test
	public void artistIsDecodedCorrectlyAsLatin9() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(ARTIST_OFFSET, 30, "Ümlaut Ärtíst! ", "ISO8859-15");
		assertThat(id3v1Tag.get().getArtist(), is(of("Ümlaut Ärtíst!")));
	}

	@Test
	public void albumIsDecodedCorrectlyAsUtf8() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(ALBUM_OFFSET, 30, "Ümlaut Älbúm! ", "UTF-8");
		assertThat(id3v1Tag.get().getAlbum(), is(of("Ümlaut Älbúm!")));
	}

	@Test
	public void albumIsDecodedCorrectlyAsLatin9() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(ALBUM_OFFSET, 30, "Ümlaut Älbúm! ", "ISO8859-15");
		assertThat(id3v1Tag.get().getAlbum(), is(of("Ümlaut Älbúm!")));
	}

	@Test
	public void YearIsCorrectlyDecoded() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(YEAR_OFFSET, 4, "2014", "UTF-8");
		assertThat(id3v1Tag.get().getDate().get().getYear(), is(2014));
	}

	@Test
	public void yearWithLettersIsDecodedAsEmpty() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(YEAR_OFFSET, 4, "201a", "UTF-8");
		assertThat(id3v1Tag.get().getDate(), is(Optional.<LocalDate>empty()));
	}

	@Test
	public void commentWithoutTrackIsParsedCorrectlyAsUtf8() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(COMMENT_OFFSET, 30, "Ümlaut Cømmént!", "UTF-8");
		assertThat(id3v1Tag.get().getComment(), is(of("Ümlaut Cømmént!")));
	}

	@Test
	public void commentWithoutTrackIsParsedCorrectlyAsLatin9() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(COMMENT_OFFSET, 30, "Ümlaut Cømmént!", "ISO8859-15");
		assertThat(id3v1Tag.get().getComment(), is(of("Ümlaut Cømmént!")));
	}

	@Test
	public void thirtyCharactersLongCommentIsNotParsedAsTrackNumber() throws UnsupportedEncodingException {
		Optional<Tag> id3v1Tag = parseTag(COMMENT_OFFSET, 30, format("%30s", "Test"), "ISO8859-15");
		assertThat(id3v1Tag.get().getTrack().isPresent(), is(false));
	}

	@Test
	public void twentyEightCharactersLongCommentAndTrackNumberAreParserCorrectly() throws UnsupportedEncodingException {
		byte[] tag = createTag(COMMENT_OFFSET, 30, "1234567890123456789012345678".getBytes("ISO8859-15"));
		tag[TRACK_OFFSET] = 4;
		Optional<Tag> id3v1Tag = parse(tag);
		assertThat(id3v1Tag.get().getComment().get().length(), is(28));
		assertThat(id3v1Tag.get().getTrack(), is(of(4)));
	}

	@Test
	public void genreIsParsedCorrectly() {
		byte[] tag = createTag(GENRE_OFFSET, 1, new byte[]{46});
		Optional<Tag> id3v1Tag = parse(tag);
		assertThat(id3v1Tag.get().getGenre(), is(getName(46)));
	}

	@Test
	public void markerForNoGenreIsRecognized() {
		byte[] tag = createTag(GENRE_OFFSET, 1, new byte[]{(byte) 255});
		Optional<Tag> id3v1Tag = parse(tag);
		assertThat(id3v1Tag.get().getGenre(), is(empty()));
	}

	@Test
	public void invalidTagIsRecognizedAndNotParsed() {
		Optional<Tag> id3v1Tag = parse(new byte[128]);
		assertThat(id3v1Tag, is(Optional.<Tag>empty()));
	}

	@Test
	public void emptyTagIsEncodable() {
		assertThat(tag.isEncodable(), is(true));
	}

	@Test
	public void tagWithNameLongerThan30CharactersIsNotEncodable() {
		tag.setName("123456789012345678901234567890");
		assertThat(tag.isEncodable(), is(true));
		tag.setName("1234567890123456789012345678901");
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithArtistLongerThan30CharactersIsNotEncodable() {
		tag.setArtist("123456789012345678901234567890");
		assertThat(tag.isEncodable(), is(true));
		tag.setArtist("1234567890123456789012345678901");
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithAlbumArtistIsNotEncodable() {
		tag.setAlbumArtist("Album Artist");
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithAlbumLongerThan30CharactersIsNotEncodable() {
		tag.setAlbum("123456789012345678901234567890");
		assertThat(tag.isEncodable(), is(true));
		tag.setAlbum("1234567890123456789012345678901");
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithNegativeTrackIsNotEncodable() {
		tag.setTrack(-1);
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithTooLargeTrackIsNotEncodable() {
		tag.setTrack(100);
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithTotalTracksIsNotEncodable() {
		tag.setTotalTracks(15);
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithDiscIsNotEncodable() {
	    tag.setDisc(1);
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithTotalDiscsIsNotEncodable() {
	    tag.setTotalDiscs(2);
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithUnknownGenreIsNotEncodable() {
		tag.setGenre("Hugendubel");
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithCommentLongerThan30CharactersIsNotEncodable() {
		tag.setComment("123456789012345678901234567890");
		assertThat(tag.isEncodable(), is(true));
		tag.setComment("1234567890123456789012345678901");
		assertThat(tag.isEncodable(), is(false));
	}

	@Test
	public void tagWithCommentLongerThan28CharactersAndATrackNumberIsNotEncodable() {
		tag.setTrack(13);
		tag.setComment("12345678901234567890123456789");
		assertThat(tag.isEncodable(), is(false));
	}

	private Optional<Tag> parseTag(int offset, int length, String value, String charsetName) throws UnsupportedEncodingException {
		byte[] tag = createTag(offset, length, value.getBytes(charsetName));
		return parse(tag);
	}

	private static byte[] createTag(int offset, int length, byte[] value) {
		byte[] tagData = createEmptyTag();
		arraycopy(value, 0, tagData, offset, min(length, value.length));
		return tagData;
	}

	private static byte[] createEmptyTag() {
		byte[] tagData = new byte[128];
		tagData[0] = 'T';
		tagData[1] = 'A';
		tagData[2] = 'G';
		return tagData;
	}

}
