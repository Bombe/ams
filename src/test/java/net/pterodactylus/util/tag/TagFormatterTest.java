package net.pterodactylus.util.tag;

import java.time.LocalDate;
import java.time.Month;

import net.pterodactylus.util.tag.TagFormatter.ExpectedLeftCurlyBrace;
import net.pterodactylus.util.tag.TagFormatter.ExpectedRightCurlyBrace;
import net.pterodactylus.util.tag.TagFormatter.InvalidTagAttribute;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link TagFormatter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TagFormatterTest {

	private final Tag tag = new Tag();

	@Test
	public void plainStringsAreEchoed() {
		TagFormatter tagFormatter = new TagFormatter("test text");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("test text"));
	}

	@Test
	public void doubledDollarSignIsEcho() {
		TagFormatter tagFormatter = new TagFormatter("test $$ text");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("test $ text"));
	}

	@Test(expected = ExpectedLeftCurlyBrace.class)
	public void dollarNotFollowedByDollarOrLeftCurlyBraceIsAnError() {
		TagFormatter tagFormatter = new TagFormatter("test $ text");
		tagFormatter.format(tag);
	}

	@Test(expected = InvalidTagAttribute.class)
	public void invalidTagAttributeThrowsException() {
		TagFormatter tagFormatter = new TagFormatter("test ${Foo}");
		tagFormatter.format(tag);
	}

	@Test(expected = ExpectedRightCurlyBrace.class)
	public void missingRightCurlyBraceThrowsException() {
		TagFormatter tagFormatter = new TagFormatter("test ${Name");
		tagFormatter.format(tag);
	}

	@Test
	public void existingArtistIsRenderedCorrectly() {
		TagFormatter tagFormatter = new TagFormatter("Artist: ${Artist}");
		tag.setArtist("Test Artist");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("Artist: Test Artist"));
	}

	@Test
	public void missingNameIsRenderedAsEmptyString() {
		TagFormatter tagFormatter = new TagFormatter("Name: ${Name} Rocks");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("Name:  Rocks"));
	}

	@Test
	public void existingAlbumArtistIsChosenOverAlternative() {
		TagFormatter tagFormatter = new TagFormatter("Name: ${AlbumArtist,Something Else}");
		tag.setAlbumArtist("On the Rocks");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("Name: On the Rocks"));
	}

	@Test
	public void nonExistingAlbumArtistIsReplacedByAlternative() {
		TagFormatter tagFormatter = new TagFormatter("Name: ${AlbumArtist,Something Else}");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("Name: Something Else"));
	}

	@Test
	public void existingAlbumIsReplacedCorrectly() {
		TagFormatter tagFormatter = new TagFormatter("${Album}");
		tag.setAlbum("Test Album");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("Test Album"));
	}

	@Test
	public void existingCommentIsReplacedCorrectly() {
		TagFormatter tagFormatter = new TagFormatter("${Comment}");
		tag.setComment("Test Comment");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("Test Comment"));
	}

	@Test
	public void trackIsReplacedCorrectly() {
		TagFormatter tagFormatter = new TagFormatter("${Track}");
		tag.setTrack(4);
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("4"));
	}

	@Test
	public void allowFormattingATrackAs3Digits() {
		TagFormatter tagFormatter = new TagFormatter("${Track%03d}");
		tag.setTrack(4);
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("004"));
	}

	@Test
	public void defaultValueForTrackIsNotFormatted() {
		TagFormatter tagFormatter = new TagFormatter("${Track%03d,0}");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("0"));
	}

	@Test
	public void allowFormattingTheNameAsRightJustified() {
		TagFormatter tagFormatter = new TagFormatter("${Name%10s}");
		tag.setName("Name");
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("      Name"));
	}

	@Test
	public void totalNumberOfTracksIsReplacedCorrectly() {
		TagFormatter tagFormatter = new TagFormatter("${TotalTracks}");
		tag.setTotalTracks(9);
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("9"));
	}

	@Test
	public void discIsReplacedCorrectly() {
		TagFormatter tagFormatter = new TagFormatter("${Disc}");
		tag.setDisc(1);
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("1"));
	}

	@Test
	public void totalDiscsIsReplacedCorrectly() {
		TagFormatter tagFormatter = new TagFormatter("${TotalDiscs}");
		tag.setTotalDiscs(2);
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("2"));
	}

	@Test
	public void dateIsReplacedAsYearOnlyCorrectly() {
		TagFormatter tagFormatter = new TagFormatter("${Date}");
		tag.setDate(LocalDate.of(2015, Month.FEBRUARY, 14));
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("2015"));
	}

	@Test
	public void customFormatterForDateCanBeUsed() {
		TagFormatter tagFormatter = new TagFormatter("${Date%tY}${Date%tm}${Date%td}");
		tag.setDate(LocalDate.of(2015, Month.FEBRUARY, 14));
		MatcherAssert.assertThat(tagFormatter.format(tag), Matchers.is("20150214"));
	}

}
