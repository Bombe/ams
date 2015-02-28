package net.pterodactylus.ams.core;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link Session}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SessionTest {

	private final Session session = new Session();

	@Test
	public void sessionRetainsAddedFiles() {
		TaggedFile file1 = Mockito.mock(TaggedFile.class);
		TaggedFile file2 = Mockito.mock(TaggedFile.class);
		session.addFile(file1);
		session.addFile(file2);
		MatcherAssert.assertThat(session.getFiles(), Matchers.containsInAnyOrder(file1, file2));
	}

	@Test
	public void addingAFileWithANameReturnsTheNameOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setName("Test Name"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getName(file), Matchers.is(Optional.of("Test Name")));
	}

	private TaggedFile createTaggedFile(Tag tag) {
		TaggedFile taggedFile = new TaggedFile(Mockito.mock(File.class)) {
			@Override
			public Optional<Tag> get() {
				return Optional.ofNullable(tag);
			}
		};
		return taggedFile;
	}

	@Test
	public void addingAFileAndSettingANameReturnsTheNewNameForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setName("Test Name"));
		session.addFile(file);
		session.setName("Override Name");
		MatcherAssert.assertThat(session.getName(file), Matchers.is(Optional.of("Override Name")));
	}

	@Test
	public void settingAnOverrideNameReturnsThatNameForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setName("Test Name"));
		session.setName("Override Name");
		MatcherAssert.assertThat(session.getName(file), Matchers.is(Optional.of("Override Name")));
	}

	@Test
	public void addingAFileWithAnArtistReturnsTheArtistOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setArtist("Test Artist"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getArtist(file), Matchers.is(Optional.of("Test Artist")));
	}

	@Test
	public void addingAFileAndSettingAnArtistReturnsTheNewArtistForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setArtist("Test Artist"));
		session.addFile(file);
		session.setArtist("Override Artist");
		MatcherAssert.assertThat(session.getArtist(file), Matchers.is(Optional.of("Override Artist")));
	}

	@Test
	public void settingAnOverrideArtistReturnsThatArtistForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setArtist("Test Artist"));
		session.setArtist("Override Artist");
		MatcherAssert.assertThat(session.getArtist(file), Matchers.is(Optional.of("Override Artist")));
	}

	@Test
	public void addingAFileWithAnAlbumReturnsTheNameOfTheAlbumForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbum("Test Album"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getAlbum(file), Matchers.is(Optional.of("Test Album")));
	}

	@Test
	public void addingAFileAndSettingAnAlbumReturnsTheNewAlbumForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbum("Test Album"));
		session.addFile(file);
		session.setAlbum("Override Album");
		MatcherAssert.assertThat(session.getAlbum(file), Matchers.is(Optional.of("Override Album")));
	}

	@Test
	public void settingAnOverrideAlbumReturnsThatAlbumForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbum("Test Album"));
		session.setAlbum("Override Album");
		MatcherAssert.assertThat(session.getAlbum(file), Matchers.is(Optional.of("Override Album")));
	}

	@Test
	public void addingAFileWithAnAlbumArtistReturnsTheNameOfTheAlbumArtistForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbumArtist("Test Album Artist"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getAlbumArtist(file), Matchers.is(Optional.of("Test Album Artist")));
	}

	@Test
	public void addingAFileAndSettingAnAlbumArtistReturnsTheNewAlbumArtistForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbumArtist("Test Album Artist"));
		session.addFile(file);
		session.setAlbumArtist("Override Album Artist");
		MatcherAssert.assertThat(session.getAlbumArtist(file), Matchers.is(Optional.of("Override Album Artist")));
	}

	@Test
	public void settingAnOverrideAlbumArtistReturnsThatAlbumArtistForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbumArtist("Test Album Artist"));
		session.setAlbumArtist("Override Album Artist");
		MatcherAssert.assertThat(session.getAlbumArtist(file), Matchers.is(Optional.of("Override Album Artist")));
	}

	@Test
	public void addingAFileWithACommentReturnsTheCommentOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setComment("Test Comment"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getComment(file), Matchers.is(Optional.of("Test Comment")));
	}

	@Test
	public void addingAFileAndSettingACommentReturnsTheNewCommentForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setComment("Test Comment"));
		session.addFile(file);
		session.setComment("Override Comment");
		MatcherAssert.assertThat(session.getComment(file), Matchers.is(Optional.of("Override Comment")));
	}

	@Test
	public void settingAnOverrideCommentReturnsThatCommentForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setComment("Test Comment"));
		session.setComment("Override Comment");
		MatcherAssert.assertThat(session.getComment(file), Matchers.is(Optional.of("Override Comment")));
	}

	@Test
	public void addingAFileWithAGenreReturnsTheGenreOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setGenre("Test Genre"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getGenre(file), Matchers.is(Optional.of("Test Genre")));
	}

	@Test
	public void addingAFileAndSettingAGenreReturnsTheNewGenreForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setGenre("Test Genre"));
		session.addFile(file);
		session.setGenre("Override Genre");
		MatcherAssert.assertThat(session.getGenre(file), Matchers.is(Optional.of("Override Genre")));
	}

	@Test
	public void settingAnOverrideGenreReturnsThatGenreForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setGenre("Test Genre"));
		session.setGenre("Override Genre");
		MatcherAssert.assertThat(session.getGenre(file), Matchers.is(Optional.of("Override Genre")));
	}

	@Test
	public void addingAFileWithATrackReturnsTheTrackOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTrack(17));
		session.addFile(file);
		MatcherAssert.assertThat(session.getTrack(file), Matchers.is(Optional.of(17)));
	}

	@Test
	public void addingAFileAndSettingATrackReturnsTheNewTrackForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTrack(17));
		session.addFile(file);
		session.setTrack(12);
		MatcherAssert.assertThat(session.getTrack(file), Matchers.is(Optional.of(12)));
	}

	@Test
	public void settingAnOverrideTrackReturnsThatTrackForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setTrack(17));
		session.setTrack(12);
		MatcherAssert.assertThat(session.getTrack(file), Matchers.is(Optional.of(12)));
	}

	@Test
	public void addingAFileWithATotalTracksReturnsTheTotalTracksOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalTracks(20));
		session.addFile(file);
		MatcherAssert.assertThat(session.getTotalTracks(file), Matchers.is(Optional.of(20)));
	}

	@Test
	public void addingAFileAndSettingATotalTracksReturnsTheNewTotalTracksForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalTracks(20));
		session.addFile(file);
		session.setTotalTracks(25);
		MatcherAssert.assertThat(session.getTotalTracks(file), Matchers.is(Optional.of(25)));
	}

	@Test
	public void settingAnOverrideTotalTracksReturnsThatTotalTracksForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalTracks(20));
		session.setTotalTracks(25);
		MatcherAssert.assertThat(session.getTotalTracks(file), Matchers.is(Optional.of(25)));
	}

	@Test
	public void addingAFileWithADiscReturnsTheDiscOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setDisc(17));
		session.addFile(file);
		MatcherAssert.assertThat(session.getDisc(file), Matchers.is(Optional.of(17)));
	}

	@Test
	public void addingAFileAndSettingADiscReturnsTheNewDiscForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setDisc(17));
		session.addFile(file);
		session.setDisc(12);
		MatcherAssert.assertThat(session.getDisc(file), Matchers.is(Optional.of(12)));
	}

	@Test
	public void settingAnOverrideDiscReturnsThatDiscForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setDisc(17));
		session.setDisc(12);
		MatcherAssert.assertThat(session.getDisc(file), Matchers.is(Optional.of(12)));
	}

	@Test
	public void addingAFileWithATotalDiscsReturnsTheTotalDiscsOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalDiscs(20));
		session.addFile(file);
		MatcherAssert.assertThat(session.getTotalDiscs(file), Matchers.is(Optional.of(20)));
	}

	@Test
	public void addingAFileAndSettingATotalDiscsReturnsTheNewTotalDiscsForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalDiscs(20));
		session.addFile(file);
		session.setTotalDiscs(25);
		MatcherAssert.assertThat(session.getTotalDiscs(file), Matchers.is(Optional.of(25)));
	}

	@Test
	public void settingAnOverrideTotalDiscsReturnsThatTotalDiscsForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalDiscs(20));
		session.setTotalDiscs(25);
		MatcherAssert.assertThat(session.getTotalDiscs(file), Matchers.is(Optional.of(25)));
	}

	@Test
	public void addingAFileWithADateReturnsTheDateOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setDate(LocalDate.of(2015, 02, 28)));
		session.addFile(file);
		MatcherAssert.assertThat(session.getDate(file), Matchers.is(Optional.of(LocalDate.of(2015, 02, 28))));
	}

	@Test
	public void addingAFileAndSettingADateReturnsTheNewDateForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setDate(LocalDate.of(2015, 02, 28)));
		session.addFile(file);
		session.setDate(LocalDate.of(2014, 9, 28));
		MatcherAssert.assertThat(session.getDate(file), Matchers.is(Optional.of(LocalDate.of(2014, 9, 28))));
	}

	@Test
	public void settingAnOverrideDateReturnsThatDateForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setDate(LocalDate.of(2015, 02, 28)));
		session.setDate(LocalDate.of(2014, 9, 28));
		MatcherAssert.assertThat(session.getDate(file), Matchers.is(Optional.of(LocalDate.of(2014, 9, 28))));
	}

}
