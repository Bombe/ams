package net.pterodactylus.util.tag.id3.v1;

import static java.lang.Math.min;
import static java.nio.charset.Charset.forName;
import static java.util.Arrays.copyOfRange;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.ALBUM_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.ARTIST_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.COMMENT_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.GENRE_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.TITLE_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.TRACK_OFFSET;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.YEAR_OFFSET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

/**
 * Unit test for {@link ID3v1TagEncoder}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagEncoderTest {

	private static final Charset utf8Charset = forName("UTF-8");
	private final ID3v1TagEncoder tagEncoder = new ID3v1TagEncoder();
	private final ID3v1Tag tag = createTag();

	private ID3v1Tag createTag() {
		ID3v1Tag id3v1Tag = new ID3v1Tag();
		id3v1Tag.setName("Test Name");
		id3v1Tag.setArtist("Test Artist");
		id3v1Tag.setAlbum("Test Album");
		id3v1Tag.setTrack(14);
		id3v1Tag.setDate(LocalDate.ofYearDay(2014, 1));
		id3v1Tag.setGenre("Metal");
		id3v1Tag.setComment("A very long comment that will be cut short by the track field.");
		return id3v1Tag;
	}

	private final byte[] tagBuffer = tagEncoder.encode(tag);

	@Test(expected = NullPointerException.class)
	public void encodingANullTagIsNotPossible() {
		tagEncoder.encode(null);
	}

	@Test
	public void tagHasTheCorrectSize() {
		assertThat(tagBuffer.length, is(128));
	}

	@Test
	public void tagHasCorrentHeader() {
		assertThat(tagBuffer, matchesAtOffset(0, 3, "TAG"));
	}

	@Test
	public void nameIsEncodedCorrectly() {
		assertThat(tagBuffer, matchesAtOffset(TITLE_OFFSET, 30, "Test Name"));
	}

	@Test
	public void artistIsEncodedCorrectly() {
		assertThat(tagBuffer, matchesAtOffset(ARTIST_OFFSET, 30, "Test Artist"));
	}

	@Test
	public void albumIsEncodedCorrectly() {
		assertThat(tagBuffer, matchesAtOffset(ALBUM_OFFSET, 30, "Test Album"));
	}

	@Test
	public void yearIsEncodedCorrectly() {
		assertThat(tagBuffer, matchesAtOffset(YEAR_OFFSET, 4, "2014"));
	}

	@Test
	public void trackIsEncodedCorrectly() {
		assertThat(tagBuffer[TRACK_OFFSET - 1], is((byte) 0));
		assertThat(tagBuffer[TRACK_OFFSET], is((byte) 14));
	}

	@Test
	public void genreIsEncodedCorrectly() {
		assertThat(tagBuffer[GENRE_OFFSET], is((byte) 9));
	}

	@Test
	public void commentIsEncodedCorrectly() {
		assertThat(tagBuffer, matchesAtOffset(COMMENT_OFFSET, 28, "A very long comment that will be cut short by the track field.".substring(0, 28)));
	}

	private Matcher<? super byte[]> matchesAtOffset(int offset, int length, String text) {
		return new TypeSafeMatcher<byte[]>() {
			@Override
			protected boolean matchesSafely(byte[] item) {
				byte[] encodedText = text.getBytes(utf8Charset);
				int textBytesToCompare = min(length, encodedText.length);
				if (!Arrays.equals(copyOfRange(item, offset, offset + textBytesToCompare), encodedText)) {
					return false;
				}
				int fillBytesToCompare = length - textBytesToCompare;
				if (!Arrays.equals(copyOfRange(item, offset + textBytesToCompare, offset + length), new byte[fillBytesToCompare])) {
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("matches ").appendValue(text).appendText(" at offset ").appendValue(offset).appendText(" for at most ").appendValue(length).appendText(" bytes");
			}

			@Override
			protected void describeMismatchSafely(byte[] item, Description mismatchDescription) {
				mismatchDescription.appendText("was ").appendValue(copyOfRange(item, offset, length));
			}
		};
	}


}
