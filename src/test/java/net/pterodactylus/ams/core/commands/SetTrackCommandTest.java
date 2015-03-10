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
 * Unit test for {@link SetTrackCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetTrackCommandTest {

	private final SetTrackCommand command = new SetTrackCommand();
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
		MatcherAssert.assertThat(command.getName(), Matchers.is("track"));
	}

	@Test
	public void trackOfAllFilesCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("1"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setTrack(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setTrack(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag().setTrack(1))
		));
	}

	@Test
	public void trackOfASingleTrackCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("-t", "2", "1"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setTrack(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void trackOfARangeOfTracksCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("-t", "1-2", "1"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setTrack(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setTrack(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void autoNumberingWorksCorrectlyOnAllFiles() throws IOException {
		command.execute(context, Arrays.asList("auto"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setTrack(1).setTotalTracks(3)),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setTrack(2).setTotalTracks(3)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag().setTrack(3).setTotalTracks(3))
		));
	}

	@Test
	public void autoNumberingWorksCorrectlyOnARange() throws IOException {
		command.execute(context, Arrays.asList("-t", "1-2", "auto"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setTrack(1).setTotalTracks(2)),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setTrack(2).setTotalTracks(2)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

}
