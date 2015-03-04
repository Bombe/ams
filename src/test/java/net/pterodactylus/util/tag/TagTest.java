package net.pterodactylus.util.tag;

import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link Tag}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TagTest {

	private final Tag tag = new Tag();
	private final LocalDate now = LocalDate.now();

	@Before
	public void setupTag() {
		tag.setName("Test Name");
		tag.setArtist("Test Artist");
		tag.setAlbumArtist("Test Album Artist");
		tag.setAlbum("Test Album");
		tag.setTrack(1);
		tag.setTotalTracks(2);
		tag.setDisc(3);
		tag.setTotalDiscs(4);
		tag.setGenre("Test Genre");
		tag.setDate(now);
		tag.setComment("Test Comment");
	}

	@Test
	public void newTagContainsNoName() {
		assertThat(new Tag().getName(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreName() {
		assertThat(tag.getName(), is(of("Test Name")));
	}

	@Test
	public void nullNameClearsTheName() {
		tag.setName(null);
		assertThat(tag.getName(), is(Optional.<String>empty()));
	}

	@Test
	public void settingNameClearsWhitespaceAtBeginningAndEnd() {
		tag.setName(" \tTest  Name\n\u00a0\r");
		MatcherAssert.assertThat(tag.getName(), Matchers.is(Optional.of("Test  Name")));
	}

	@Test
	public void newTagContainsNoArtist() {
		assertThat(new Tag().getArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreArtist() {
		assertThat(tag.getArtist(), is(of("Test Artist")));
	}

	@Test
	public void settingArtistStripsWhitespaceAtBeginningAndEnd() {
		tag.setArtist(" \tTest  Artist\n\u00a0\r");
		MatcherAssert.assertThat(tag.getArtist(), Matchers.is(Optional.of("Test  Artist")));
	}

	@Test
	public void nullArtistClearTheArtist() {
		tag.setArtist(null);
		assertThat(tag.getArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoAlbumArtist() {
		assertThat(new Tag().getAlbumArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreAlbumArtist() {
		assertThat(tag.getAlbumArtist(), is(of("Test Album Artist")));
	}

	@Test
	public void settingAlbumArtistStripsWhitespaceAtBeginningAndEnd() {
		tag.setAlbumArtist(" \tTest  Album Artist\n\u00a0\r");
		MatcherAssert.assertThat(tag.getAlbumArtist(), Matchers.is(Optional.of("Test  Album Artist")));
	}

	@Test
	public void nullAlbumArtistClearsTheAlbumArtist() {
		tag.setAlbumArtist(null);
		assertThat(tag.getAlbumArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoAlbum() {
		assertThat(new Tag().getAlbum(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreAlbum() {
		assertThat(tag.getAlbum(), is(of("Test Album")));
	}

	@Test
	public void settingAlbumStripsWhitespaceAtBeginningAndEnd() {
		tag.setAlbum(" \tTest  Album\n\u00a0\r");
		MatcherAssert.assertThat(tag.getAlbum(), Matchers.is(Optional.of("Test  Album")));
	}

	@Test
	public void nullAlbumClearsTheAlbum() {
		tag.setAlbum(null);
		assertThat(tag.getAlbum(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoTrack() {
		assertThat(new Tag().getTrack(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTrack() {
		assertThat(tag.getTrack(), is(of(1)));
	}

	@Test
	public void zeroTrackClearsTheTrack() {
		tag.setTrack(0);
		assertThat(tag.getTrack(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoTotalTracks() {
		assertThat(new Tag().getTotalTracks(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTotalTracks() {
		assertThat(tag.getTotalTracks(), is(of(2)));
	}

	@Test
	public void zeroTotalTracksClearsTheTotalTracks() {
		tag.setTotalTracks(0);
		assertThat(tag.getTotalTracks(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoDisc() {
		assertThat(new Tag().getDisc(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreDisc() {
		assertThat(tag.getDisc(), is(of(3)));
	}

	@Test
	public void zeroDiscClearsTheDisc() {
		tag.setDisc(0);
		assertThat(tag.getDisc(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoTotalDiscs() {
		assertThat(new Tag().getTotalDiscs(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTotalDiscs() {
		assertThat(tag.getTotalDiscs(), is(of(4)));
	}

	@Test
	public void zeroTotalDiscsClearsTheTotalDiscs() {
		tag.setTotalDiscs(0);
		assertThat(tag.getTotalDiscs(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoGenre() {
		assertThat(new Tag().getGenre(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreGenre() {
		assertThat(tag.getGenre(), is(of("Test Genre")));
	}

	@Test
	public void nullGenreClearsTheGenre() {
		tag.setGenre(null);
		assertThat(tag.getGenre(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoDate() {
		assertThat(new Tag().getDate(), is(Optional.<LocalDate>empty()));
	}

	@Test
	public void canStoreDate() {
		assertThat(tag.getDate(), is(of(now)));
	}

	@Test
	public void nullDateClearsTheDate() {
		tag.setDate(null);
		assertThat(tag.getDate(), is(Optional.<LocalDate>empty()));
	}

	@Test
	public void newTagContainsNoComment() {
		assertThat(new Tag().getComment(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreComment() {
		assertThat(tag.getComment(), is(of("Test Comment")));
	}

	@Test
	public void settingCommentStripsWhitespaceAtBeginningAndEnd() {
		tag.setComment(" \tTest  Comment\n\u00a0\r");
		MatcherAssert.assertThat(tag.getComment(), Matchers.is(Optional.of("Test  Comment")));
	}

	@Test
	public void nullCommentClearsTheComment() {
		tag.setComment(null);
		assertThat(tag.getComment(), is(Optional.<String>empty()));
	}

	@Test
	public void copyConstructorCopiesTag() {
		Tag newTag = new Tag(tag);
		MatcherAssert.assertThat(newTag.getName().get(), Matchers.is("Test Name"));
		MatcherAssert.assertThat(newTag.getArtist().get(), Matchers.is("Test Artist"));
		MatcherAssert.assertThat(newTag.getAlbumArtist().get(), Matchers.is("Test Album Artist"));
		MatcherAssert.assertThat(newTag.getAlbum().get(), Matchers.is("Test Album"));
		MatcherAssert.assertThat(newTag.getTrack().get(), Matchers.is(1));
		MatcherAssert.assertThat(newTag.getTotalTracks().get(), Matchers.is(2));
		MatcherAssert.assertThat(newTag.getDisc().get(), Matchers.is(3));
		MatcherAssert.assertThat(newTag.getTotalDiscs().get(), Matchers.is(4));
		MatcherAssert.assertThat(newTag.getGenre().get(), Matchers.is("Test Genre"));
		MatcherAssert.assertThat(newTag.getDate().get(), Matchers.is(now));
		MatcherAssert.assertThat(newTag.getComment().get(), Matchers.is("Test Comment"));
	}

	@Test
	public void copiedTagIsEqualToSource() {
		Tag newTag = new Tag(tag);
		MatcherAssert.assertThat(newTag, Matchers.is(tag));
		MatcherAssert.assertThat(newTag.hashCode(), Matchers.is(tag.hashCode()));
	}

}
