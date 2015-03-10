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
 * Unit test for {@link SetDiscCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetDiscCommandTest {

	private final SetDiscCommand command = new SetDiscCommand();
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
		MatcherAssert.assertThat(command.getName(), Matchers.is("disc"));
	}

	@Test
	public void discOfAllFilesCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("1"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setDisc(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setDisc(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag().setDisc(1))
		));
	}

	@Test
	public void discOfASingleTrackCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("-t", "2", "1"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setDisc(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void discOfARangeOfTracksCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("--tracks", "1-2", "1"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setDisc(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setDisc(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void autoNumberingOnMissingTracksSetsAllDiscsToOne() throws IOException {
		command.execute(context, Arrays.asList("auto"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setDisc(1).setTotalDiscs(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setDisc(1).setTotalDiscs(1)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag().setDisc(1).setTotalDiscs(1))
		));
	}

	@Test
	public void autoNumberingIncreasesDiscsCorrectly() throws IOException {
		session.getFiles().get(1).getTag().setTrack(1);
		command.execute(context, Arrays.asList("auto"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setDisc(1).setTotalDiscs(2)),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setTrack(1).setDisc(1).setTotalDiscs(2)),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag().setDisc(2).setTotalDiscs(2))
		));
	}

}
