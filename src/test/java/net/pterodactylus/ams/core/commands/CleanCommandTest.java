package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link CleanCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CleanCommandTest {

	private static final String DIRTY_ARTIST = "the artist";
	private static final String DIRTY_NAME = "song by an ARTIST";
	private static final String DIRTY_ALBUM = "a name fRoM the ALbum";
	private static final String DIRTY_ALBUM_ARTIST = "name an album artist of";
	private static final String DIRTY_COMMAND = "comment for the album";
	private static final String DIRTY_GENRE = "THE STYLE THE STUFF GOES BY";
	private static final String CLEAN_ARTIST = "The Artist";
	private static final String CLEAN_NAME = "Song by an Artist";
	private static final String CLEAN_ALBUM = "A Name From the Album";
	private static final String CLEAN_ALBUM_ARTIST = "Name an Album Artist Of";
	private static final String CLEAN_COMMENT = "Comment for the Album";
	private static final String CLEAN_GENRE = "The Style the Stuff Goes By";

	private final CleanCommand command = new CleanCommand();
	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();

	@Before
	public void setupSession() {
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setArtist(DIRTY_ARTIST)));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setName(DIRTY_NAME)));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setAlbum(DIRTY_ALBUM)));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setAlbumArtist(DIRTY_ALBUM_ARTIST)));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setComment(DIRTY_COMMAND)));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setGenre(DIRTY_GENRE)));
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.is("clean"));
	}

	@Test
	public void cleaningCorrectlyCapitalizesAllWords() throws IOException {
		command.execute(context, Collections.emptyList());
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()),
				Matchers.contains(
						new Tag().setArtist(CLEAN_ARTIST),
						new Tag().setName(CLEAN_NAME),
						new Tag().setAlbum(CLEAN_ALBUM),
						new Tag().setAlbumArtist(CLEAN_ALBUM_ARTIST),
						new Tag().setComment(CLEAN_COMMENT),
						new Tag().setGenre(CLEAN_GENRE)
				));
	}

	@Test
	public void cleaningCanBeRestrictedToSelectedFiles() throws IOException {
		command.execute(context, Arrays.asList("-t", "1-2"));
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()),
				Matchers.contains(
						new Tag().setArtist(CLEAN_ARTIST),
						new Tag().setName(CLEAN_NAME),
						new Tag().setAlbum(DIRTY_ALBUM),
						new Tag().setAlbumArtist(DIRTY_ALBUM_ARTIST),
						new Tag().setComment(DIRTY_COMMAND),
						new Tag().setGenre(DIRTY_GENRE)
				));
	}

	@Test
	public void cleaningCanBeRestrictedToArtistValue() throws IOException {
		command.execute(context, Arrays.asList("--artist"));
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()),
				Matchers.contains(
						new Tag().setArtist(CLEAN_ARTIST),
						new Tag().setName(DIRTY_NAME),
						new Tag().setAlbum(DIRTY_ALBUM),
						new Tag().setAlbumArtist(DIRTY_ALBUM_ARTIST),
						new Tag().setComment(DIRTY_COMMAND),
						new Tag().setGenre(DIRTY_GENRE)
				));
	}

	@Test
	public void cleaningCanBeRestrictedToNameAndAlbumValue() throws IOException {
		command.execute(context, Arrays.asList("--name", "--album"));
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()),
				Matchers.contains(
						new Tag().setArtist(DIRTY_ARTIST),
						new Tag().setName(CLEAN_NAME),
						new Tag().setAlbum(CLEAN_ALBUM),
						new Tag().setAlbumArtist(DIRTY_ALBUM_ARTIST),
						new Tag().setComment(DIRTY_COMMAND),
						new Tag().setGenre(DIRTY_GENRE)
				));
	}

	@Test
	public void cleaningCanBeRestrictedBothBySelectedFilesAndTagValues() throws IOException {
		command.execute(context, Arrays.asList("-t", "4-", "--album", "--albumartist", "--comment", "--genre"));
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()),
				Matchers.contains(
						new Tag().setArtist(DIRTY_ARTIST),
						new Tag().setName(DIRTY_NAME),
						new Tag().setAlbum(DIRTY_ALBUM),
						new Tag().setAlbumArtist(CLEAN_ALBUM_ARTIST),
						new Tag().setComment(CLEAN_COMMENT),
						new Tag().setGenre(CLEAN_GENRE)
				));
	}

}
