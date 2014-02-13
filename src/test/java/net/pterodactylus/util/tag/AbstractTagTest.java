package net.pterodactylus.util.tag;

import static java.time.LocalDate.now;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;

/**
 * Unit test for {@link AbstractTag}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AbstractTagTest {

	private final AbstractTag abstractTag = new AbstractTag() {
		@Override
		public boolean isEncodable() {
			return false;
		}

		@Override
		public boolean write(File file) throws IOException {
			return false;
		}
	};

	@Test
	public void newTagContainsNoName() {
		assertThat(abstractTag.getName(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreName() {
		abstractTag.setName("Test Name");
		assertThat(abstractTag.getName(), is(of("Test Name")));
	}

	@Test
	public void nullNameClearsTheName() {
		abstractTag.setName("Test Name");
		abstractTag.setName(null);
		assertThat(abstractTag.getName(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoArtist() {
		assertThat(abstractTag.getArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreArtist() {
		abstractTag.setArtist("Test Artist");
		assertThat(abstractTag.getArtist(), is(of("Test Artist")));
	}

	@Test
	public void nullArtistClearTheArtist() {
		abstractTag.setArtist("Test Artist");
		abstractTag.setArtist(null);
		assertThat(abstractTag.getArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoAlbumArtist() {
		assertThat(abstractTag.getAlbumArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreAlbumArtist() {
		abstractTag.setAlbumArtist("Test Album Artist");
		assertThat(abstractTag.getAlbumArtist(), is(of("Test Album Artist")));
	}

	@Test
	public void nullAlbumArtistClearsTheAlbumArtist() {
		abstractTag.setAlbumArtist("Test Album Artist");
		abstractTag.setAlbumArtist(null);
		assertThat(abstractTag.getAlbumArtist(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoAlbum() {
		assertThat(abstractTag.getAlbum(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreAlbum() {
		abstractTag.setAlbum("Test Album");
		assertThat(abstractTag.getAlbum(), is(of("Test Album")));
	}

	@Test
	public void nullAlbumClearsTheAlbum() {
		abstractTag.setAlbum("Test Album");
		abstractTag.setAlbum(null);
		assertThat(abstractTag.getAlbum(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoTrack() {
		assertThat(abstractTag.getTrack(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTrack() {
		abstractTag.setTrack(13);
		assertThat(abstractTag.getTrack(), is(of(13)));
	}

	@Test
	public void zeroTrackClearsTheTrack() {
		abstractTag.setTrack(13);
		abstractTag.setTrack(0);
		assertThat(abstractTag.getTrack(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoTotalTracks() {
		assertThat(abstractTag.getTotalTracks(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTotalTracks() {
		abstractTag.setTotalTracks(13);
		assertThat(abstractTag.getTotalTracks(), is(of(13)));
	}

	@Test
	public void zeroTotalTracksClearsTheTotalTracks() {
		abstractTag.setTotalTracks(13);
		abstractTag.setTotalTracks(0);
		assertThat(abstractTag.getTotalTracks(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoDisc() {
		assertThat(abstractTag.getDisc(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreDisc() {
		abstractTag.setDisc(13);
		assertThat(abstractTag.getDisc(), is(of(13)));
	}

	@Test
	public void zeroDiscClearsTheDisc() {
		abstractTag.setDisc(13);
		abstractTag.setDisc(0);
		assertThat(abstractTag.getDisc(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoTotalDiscs() {
		assertThat(abstractTag.getTotalDiscs(), is(Optional.<Integer>empty()));
	}

	@Test
	public void canStoreTotalDiscs() {
		abstractTag.setTotalDiscs(13);
		assertThat(abstractTag.getTotalDiscs(), is(of(13)));
	}

	@Test
	public void zeroTotalDiscsClearsTheTotalDiscs() {
		abstractTag.setTotalDiscs(13);
		abstractTag.setTotalDiscs(0);
		assertThat(abstractTag.getTotalDiscs(), is(Optional.<Integer>empty()));
	}

	@Test
	public void newTagContainsNoGenre() {
		assertThat(abstractTag.getGenre(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreGenre() {
		abstractTag.setGenre("Test Genre");
		assertThat(abstractTag.getGenre(), is(of("Test Genre")));
	}

	@Test
	public void nullGenreClearsTheGenre() {
		abstractTag.setGenre("Test Genre");
		abstractTag.setGenre(null);
		assertThat(abstractTag.getGenre(), is(Optional.<String>empty()));
	}

	@Test
	public void newTagContainsNoDate() {
		assertThat(abstractTag.getDate(), is(Optional.<LocalDate>empty()));
	}

	@Test
	public void canStoreDate() {
		LocalDate now = now();
		abstractTag.setDate(now);
		assertThat(abstractTag.getDate(), is(of(now)));
	}

	@Test
	public void nullDateClearsTheDate() {
		abstractTag.setDate(now());
		abstractTag.setDate(null);
		assertThat(abstractTag.getDate(), is(Optional.<LocalDate>empty()));
	}

	@Test
	public void newTagContainsNoComment() {
		assertThat(abstractTag.getComment(), is(Optional.<String>empty()));
	}

	@Test
	public void canStoreComment() {
		abstractTag.setComment("Test Comment");
		assertThat(abstractTag.getComment(), is(of("Test Comment")));
	}

	@Test
	public void nullCommentClearsTheComment() {
		abstractTag.setComment("Test Comment");
		abstractTag.setComment(null);
		assertThat(abstractTag.getComment(), is(Optional.<String>empty()));
	}

}
