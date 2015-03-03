package net.pterodactylus.ams.core;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

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
		TaggedFile file1 = createTaggedFile(new Tag());
		TaggedFile file2 = createTaggedFile(new Tag());
		session.addFile(file1);
		session.addFile(file2);
		MatcherAssert.assertThat(session.getFiles(), Matchers.containsInAnyOrder(file1, file2));
	}

	@Test
	public void addingAFileAndGettingTheTagReturnsATagWithTheSameValues() {
		Tag tag = new Tag().setName("Test Name");
		TaggedFile file = createTaggedFile(tag);
		session.addFile(file);
		MatcherAssert.assertThat(session.getTag(file), Matchers.is(tag));
	}

	@Test(expected = IllegalArgumentException.class)
	public void gettingATagForAFileThatWasNotAddedThrowsAnException() {
		TaggedFile file = createTaggedFile(new Tag().setName("Test Name"));
		session.getTag(file);
	}

	@Test
	public void addingAFileWithANameReturnsTheNameOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setName("Test Name"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getName(file), Matchers.is(Optional.of("Test Name")));
		MatcherAssert.assertThat(session.isSessionName(), Matchers.is(false));
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
		MatcherAssert.assertThat(session.isSessionName(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideNameReturnsThatNameForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setName("Test Name"));
		session.setName("Override Name");
		MatcherAssert.assertThat(session.getName(file), Matchers.is(Optional.of("Override Name")));
		MatcherAssert.assertThat(session.isSessionName(), Matchers.is(true));
	}

	@Test
	public void modifiedNameOfAFilesTagInTheSessionRetainsModification() {
		TaggedFile file = createTaggedFile(new Tag().setName("Test Name"));
		session.addFile(file);
		session.getTag(file).setName("Override Name");
		MatcherAssert.assertThat(session.getName(file).get(), Matchers.is("Override Name"));
	}

	@Test
	public void modifyingTheNameOfAFilesTagInTheSessionDoesNotModifyTheFilesTag() {
		TaggedFile file = createTaggedFile(new Tag().setName("Test Name"));
		session.addFile(file);
		session.getTag(file).setName("Override Name");
		MatcherAssert.assertThat(file.get().get().getName().get(), Matchers.is("Test Name"));
	}

	@Test
	public void addingAFileWithAnArtistReturnsTheArtistOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setArtist("Test Artist"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getArtist(file), Matchers.is(Optional.of("Test Artist")));
		MatcherAssert.assertThat(session.isSessionArtist(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingAnArtistReturnsTheNewArtistForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setArtist("Test Artist"));
		session.addFile(file);
		session.setArtist("Override Artist");
		MatcherAssert.assertThat(session.getArtist(file), Matchers.is(Optional.of("Override Artist")));
		MatcherAssert.assertThat(session.isSessionArtist(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideArtistReturnsThatArtistForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setArtist("Test Artist"));
		session.setArtist("Override Artist");
		MatcherAssert.assertThat(session.getArtist(file), Matchers.is(Optional.of("Override Artist")));
		MatcherAssert.assertThat(session.isSessionArtist(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithAnAlbumReturnsTheNameOfTheAlbumForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbum("Test Album"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getAlbum(file), Matchers.is(Optional.of("Test Album")));
		MatcherAssert.assertThat(session.isSessionAlbum(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingAnAlbumReturnsTheNewAlbumForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbum("Test Album"));
		session.addFile(file);
		session.setAlbum("Override Album");
		MatcherAssert.assertThat(session.getAlbum(file), Matchers.is(Optional.of("Override Album")));
		MatcherAssert.assertThat(session.isSessionAlbum(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideAlbumReturnsThatAlbumForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbum("Test Album"));
		session.setAlbum("Override Album");
		MatcherAssert.assertThat(session.getAlbum(file), Matchers.is(Optional.of("Override Album")));
		MatcherAssert.assertThat(session.isSessionAlbum(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithAnAlbumArtistReturnsTheNameOfTheAlbumArtistForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbumArtist("Test Album Artist"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getAlbumArtist(file), Matchers.is(Optional.of("Test Album Artist")));
		MatcherAssert.assertThat(session.isSessionAlbumArtist(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingAnAlbumArtistReturnsTheNewAlbumArtistForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbumArtist("Test Album Artist"));
		session.addFile(file);
		session.setAlbumArtist("Override Album Artist");
		MatcherAssert.assertThat(session.getAlbumArtist(file), Matchers.is(Optional.of("Override Album Artist")));
		MatcherAssert.assertThat(session.isSessionAlbumArtist(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideAlbumArtistReturnsThatAlbumArtistForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setAlbumArtist("Test Album Artist"));
		session.setAlbumArtist("Override Album Artist");
		MatcherAssert.assertThat(session.getAlbumArtist(file), Matchers.is(Optional.of("Override Album Artist")));
		MatcherAssert.assertThat(session.isSessionAlbumArtist(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithACommentReturnsTheCommentOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setComment("Test Comment"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getComment(file), Matchers.is(Optional.of("Test Comment")));
		MatcherAssert.assertThat(session.isSessionComment(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingACommentReturnsTheNewCommentForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setComment("Test Comment"));
		session.addFile(file);
		session.setComment("Override Comment");
		MatcherAssert.assertThat(session.getComment(file), Matchers.is(Optional.of("Override Comment")));
		MatcherAssert.assertThat(session.isSessionComment(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideCommentReturnsThatCommentForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setComment("Test Comment"));
		session.setComment("Override Comment");
		MatcherAssert.assertThat(session.getComment(file), Matchers.is(Optional.of("Override Comment")));
		MatcherAssert.assertThat(session.isSessionComment(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithAGenreReturnsTheGenreOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setGenre("Test Genre"));
		session.addFile(file);
		MatcherAssert.assertThat(session.getGenre(file), Matchers.is(Optional.of("Test Genre")));
		MatcherAssert.assertThat(session.isSessionGenre(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingAGenreReturnsTheNewGenreForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setGenre("Test Genre"));
		session.addFile(file);
		session.setGenre("Override Genre");
		MatcherAssert.assertThat(session.getGenre(file), Matchers.is(Optional.of("Override Genre")));
		MatcherAssert.assertThat(session.isSessionGenre(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideGenreReturnsThatGenreForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setGenre("Test Genre"));
		session.setGenre("Override Genre");
		MatcherAssert.assertThat(session.getGenre(file), Matchers.is(Optional.of("Override Genre")));
		MatcherAssert.assertThat(session.isSessionGenre(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithATrackReturnsTheTrackOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTrack(17));
		session.addFile(file);
		MatcherAssert.assertThat(session.getTrack(file), Matchers.is(Optional.of(17)));
		MatcherAssert.assertThat(session.isSessionTrack(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingATrackReturnsTheNewTrackForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTrack(17));
		session.addFile(file);
		session.setTrack(12);
		MatcherAssert.assertThat(session.getTrack(file), Matchers.is(Optional.of(12)));
		MatcherAssert.assertThat(session.isSessionTrack(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideTrackReturnsThatTrackForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setTrack(17));
		session.setTrack(12);
		MatcherAssert.assertThat(session.getTrack(file), Matchers.is(Optional.of(12)));
		MatcherAssert.assertThat(session.isSessionTrack(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithATotalTracksReturnsTheTotalTracksOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalTracks(20));
		session.addFile(file);
		MatcherAssert.assertThat(session.getTotalTracks(file), Matchers.is(Optional.of(20)));
		MatcherAssert.assertThat(session.isSessionTotalTracks(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingATotalTracksReturnsTheNewTotalTracksForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalTracks(20));
		session.addFile(file);
		session.setTotalTracks(25);
		MatcherAssert.assertThat(session.getTotalTracks(file), Matchers.is(Optional.of(25)));
		MatcherAssert.assertThat(session.isSessionTotalTracks(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideTotalTracksReturnsThatTotalTracksForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalTracks(20));
		session.setTotalTracks(25);
		MatcherAssert.assertThat(session.getTotalTracks(file), Matchers.is(Optional.of(25)));
		MatcherAssert.assertThat(session.isSessionTotalTracks(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithADiscReturnsTheDiscOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setDisc(17));
		session.addFile(file);
		MatcherAssert.assertThat(session.getDisc(file), Matchers.is(Optional.of(17)));
		MatcherAssert.assertThat(session.isSessionDisc(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingADiscReturnsTheNewDiscForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setDisc(17));
		session.addFile(file);
		session.setDisc(12);
		MatcherAssert.assertThat(session.getDisc(file), Matchers.is(Optional.of(12)));
		MatcherAssert.assertThat(session.isSessionDisc(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideDiscReturnsThatDiscForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setDisc(17));
		session.setDisc(12);
		MatcherAssert.assertThat(session.getDisc(file), Matchers.is(Optional.of(12)));
		MatcherAssert.assertThat(session.isSessionDisc(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithATotalDiscsReturnsTheTotalDiscsOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalDiscs(20));
		session.addFile(file);
		MatcherAssert.assertThat(session.getTotalDiscs(file), Matchers.is(Optional.of(20)));
		MatcherAssert.assertThat(session.isSessionTotalDiscs(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingATotalDiscsReturnsTheNewTotalDiscsForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalDiscs(20));
		session.addFile(file);
		session.setTotalDiscs(25);
		MatcherAssert.assertThat(session.getTotalDiscs(file), Matchers.is(Optional.of(25)));
		MatcherAssert.assertThat(session.isSessionTotalDiscs(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideTotalDiscsReturnsThatTotalDiscsForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setTotalDiscs(20));
		session.setTotalDiscs(25);
		MatcherAssert.assertThat(session.getTotalDiscs(file), Matchers.is(Optional.of(25)));
		MatcherAssert.assertThat(session.isSessionTotalDiscs(), Matchers.is(true));
	}

	@Test
	public void addingAFileWithADateReturnsTheDateOfTheFileForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setDate(LocalDate.of(2015, 02, 28)));
		session.addFile(file);
		MatcherAssert.assertThat(session.getDate(file), Matchers.is(Optional.of(LocalDate.of(2015, 02, 28))));
		MatcherAssert.assertThat(session.isSessionDate(), Matchers.is(false));
	}

	@Test
	public void addingAFileAndSettingADateReturnsTheNewDateForTheFile() {
		TaggedFile file = createTaggedFile(new Tag().setDate(LocalDate.of(2015, 02, 28)));
		session.addFile(file);
		session.setDate(LocalDate.of(2014, 9, 28));
		MatcherAssert.assertThat(session.getDate(file), Matchers.is(Optional.of(LocalDate.of(2014, 9, 28))));
		MatcherAssert.assertThat(session.isSessionDate(), Matchers.is(true));
	}

	@Test
	public void settingAnOverrideDateReturnsThatDateForANonExistingFile() {
		TaggedFile file = createTaggedFile(new Tag().setDate(LocalDate.of(2015, 02, 28)));
		session.setDate(LocalDate.of(2014, 9, 28));
		MatcherAssert.assertThat(session.getDate(file), Matchers.is(Optional.of(LocalDate.of(2014, 9, 28))));
		MatcherAssert.assertThat(session.isSessionDate(), Matchers.is(true));
	}

}
