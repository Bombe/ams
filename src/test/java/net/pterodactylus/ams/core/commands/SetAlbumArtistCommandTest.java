package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;
import net.pterodactylus.util.tag.TaggedFileTest;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link SetAlbumArtistCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetAlbumArtistCommandTest {

	private final SetAlbumArtistCommand command = new SetAlbumArtistCommand();
	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();

	@Before
	public void setupSession() {
		session.addFile(new TaggedFile(Paths.get("/foo/bar/file1.music"), new Tag()));
		session.addFile(new TaggedFile(Paths.get("/foo/bar/file2.music"), new Tag()));
		session.addFile(new TaggedFile(Paths.get("/foo/bar/file3.music"), new Tag()));
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.is("albumartist"));
	}

	@Test
	public void albumArtistOfAllFilesCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("New", "Album Artist"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setAlbumArtist("New Album Artist")),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setAlbumArtist("New Album Artist")),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag().setAlbumArtist("New Album Artist"))
		));
	}

	@Test
	public void albumArtistOfASingleTrackCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("-t", "2", "New", "Album Artist"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setAlbumArtist("New Album Artist")),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void albumArtistOfARangeOfTracksCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("--tracks", "1-2", "New", "Album Artist"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setAlbumArtist("New Album Artist")),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setAlbumArtist("New Album Artist")),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

}
