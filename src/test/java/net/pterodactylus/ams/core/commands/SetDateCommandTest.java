package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
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
 * Unit test for {@link SetDateCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetDateCommandTest {

	private final SetDateCommand command = new SetDateCommand();
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
		MatcherAssert.assertThat(command.getName(), Matchers.is("date"));
	}

	@Test
	public void dateOfAllFilesCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("2015"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setDate(LocalDate.of(2015, 1, 1))),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setDate(LocalDate.of(2015, 1, 1))),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag().setDate(LocalDate.of(2015, 1, 1)))
		));
	}

	@Test
	public void dateOfASingleTrackCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("-t", "2", "2015-03-04"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setDate(LocalDate.of(2015, 3, 4))),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void dateOfARangeOfTracksCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("--tracks", "1-2", "2015"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setDate(LocalDate.of(2015, 1, 1))),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setDate(LocalDate.of(2015, 1, 1))),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void invalidDateDoesNotModifyTag() throws IOException {
		command.execute(context, Arrays.asList("invalid"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

}
