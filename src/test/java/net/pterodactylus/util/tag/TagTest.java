package net.pterodactylus.util.tag;

import static java.time.LocalDate.now;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link Tag}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TagTest {

	private final Tag tag = new Tag();

	@Test
	public void newTagContainsNoName() {
		assertThat(tag.getName(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreName() {
		tag.setName("Test Name");
		assertThat(tag.getName(), is(of("Test Name")));
	}

	@Test
	public void nullNameClearsTheName() {
		tag.setName("Test Name");
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
		assertThat(tag.getArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreArtist() {
		tag.setArtist("Test Artist");
		assertThat(tag.getArtist(), is(of("Test Artist")));
	}

	@Test
	public void settingArtistStripsWhitespaceAtBeginningAndEnd() {
		tag.setArtist(" \tTest  Artist\n\u00a0\r");
		MatcherAssert.assertThat(tag.getArtist(), Matchers.is(Optional.of("Test  Artist")));
	}

	@Test
	public void nullArtistClearTheArtist() {
		tag.setArtist("Test Artist");
		tag.setArtist(null);
		assertThat(tag.getArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoAlbumArtist() {
		assertThat(tag.getAlbumArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreAlbumArtist() {
		tag.setAlbumArtist("Test Album Artist");
		assertThat(tag.getAlbumArtist(), is(of("Test Album Artist")));
	}

	@Test
	public void settingAlbumArtistStripsWhitespaceAtBeginningAndEnd() {
		tag.setAlbumArtist(" \tTest  Album Artist\n\u00a0\r");
		MatcherAssert.assertThat(tag.getAlbumArtist(), Matchers.is(Optional.of("Test  Album Artist")));
	}

	@Test
	public void nullAlbumArtistClearsTheAlbumArtist() {
		tag.setAlbumArtist("Test Album Artist");
		tag.setAlbumArtist(null);
		assertThat(tag.getAlbumArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoAlbum() {
		assertThat(tag.getAlbum(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreAlbum() {
		tag.setAlbum("Test Album");
		assertThat(tag.getAlbum(), is(of("Test Album")));
	}

	@Test
	public void settingAlbumStripsWhitespaceAtBeginningAndEnd() {
		tag.setAlbum(" \tTest  Album\n\u00a0\r");
		MatcherAssert.assertThat(tag.getAlbum(), Matchers.is(Optional.of("Test  Album")));
	}

	@Test
	public void nullAlbumClearsTheAlbum() {
		tag.setAlbum("Test Album");
		tag.setAlbum(null);
		assertThat(tag.getAlbum(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoTrack() {
		assertThat(tag.getTrack(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTrack() {
		tag.setTrack(13);
		assertThat(tag.getTrack(), is(of(13)));
	}

	@Test
	public void zeroTrackClearsTheTrack() {
		tag.setTrack(13);
		tag.setTrack(0);
		assertThat(tag.getTrack(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoTotalTracks() {
		assertThat(tag.getTotalTracks(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTotalTracks() {
		tag.setTotalTracks(13);
		assertThat(tag.getTotalTracks(), is(of(13)));
	}

	@Test
	public void zeroTotalTracksClearsTheTotalTracks() {
		tag.setTotalTracks(13);
		tag.setTotalTracks(0);
		assertThat(tag.getTotalTracks(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoDisc() {
		assertThat(tag.getDisc(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreDisc() {
		tag.setDisc(13);
		assertThat(tag.getDisc(), is(of(13)));
	}

	@Test
	public void zeroDiscClearsTheDisc() {
		tag.setDisc(13);
		tag.setDisc(0);
		assertThat(tag.getDisc(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoTotalDiscs() {
		assertThat(tag.getTotalDiscs(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTotalDiscs() {
		tag.setTotalDiscs(13);
		assertThat(tag.getTotalDiscs(), is(of(13)));
	}

	@Test
	public void zeroTotalDiscsClearsTheTotalDiscs() {
		tag.setTotalDiscs(13);
		tag.setTotalDiscs(0);
		assertThat(tag.getTotalDiscs(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoGenre() {
		assertThat(tag.getGenre(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreGenre() {
		tag.setGenre("Test Genre");
		assertThat(tag.getGenre(), is(of("Test Genre")));
	}

	@Test
	public void nullGenreClearsTheGenre() {
		tag.setGenre("Test Genre");
		tag.setGenre(null);
		assertThat(tag.getGenre(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoDate() {
		assertThat(tag.getDate(), is(Optional.<LocalDate>empty()));
	}

	@Test
	public void canStoreDate() {
		LocalDate now = now();
		tag.setDate(now);
		assertThat(tag.getDate(), is(of(now)));
	}

	@Test
	public void nullDateClearsTheDate() {
		tag.setDate(now());
		tag.setDate(null);
		assertThat(tag.getDate(), is(Optional.<LocalDate>empty()));
	}

	@Test
	public void newTagContainsNoComment() {
		assertThat(tag.getComment(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreComment() {
		tag.setComment("Test Comment");
		assertThat(tag.getComment(), is(of("Test Comment")));
	}

	@Test
	public void settingCommentStripsWhitespaceAtBeginningAndEnd() {
		tag.setComment(" \tTest  Comment\n\u00a0\r");
		MatcherAssert.assertThat(tag.getComment(), Matchers.is(Optional.of("Test  Comment")));
	}

	@Test
	public void nullCommentClearsTheComment() {
		tag.setComment("Test Comment");
		tag.setComment(null);
		assertThat(tag.getComment(), is(Optional.<String>empty()));
	}

}
