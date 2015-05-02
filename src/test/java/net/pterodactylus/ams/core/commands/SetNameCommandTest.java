package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;
import net.pterodactylus.util.tag.TaggedFileTest;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link SetNameCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetNameCommandTest {

	private final SetNameCommand command = new SetNameCommand();
	private final Session session = new Session();
	private final Context context = Mockito.mock(Context.class);

	@Before
	public void setupSession() {
		session.addFile(new TaggedFile(Paths.get("/foo/bar/file1.music"), new Tag()));
		session.addFile(new TaggedFile(Paths.get("/foo/bar/file2.music"), new Tag()));
		session.addFile(new TaggedFile(Paths.get("/foo/bar/file3.music"), new Tag()));
		Mockito.when(context.getSession()).thenReturn(session);
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.is("name"));
	}

	@Test
	public void nameOfAllFilesCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("New", "Name"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setName("New Name")),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void nameOfASingleTrackCanBeSet() throws IOException {
		command.execute(context, Arrays.asList("-t", "2", "New", "Name"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setName("New Name")),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void nameOfARangeOfTracksCanBeSet() throws IOException {
		Mockito.when(context.getNextLine()).thenReturn("Other Name");
		command.execute(context, Arrays.asList("--tracks", "1-2", "New", "Name"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setName("New Name")),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setName("Other Name")),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

	@Test
	public void namesOfAllFilesAreReadFromSupplier() throws IOException {
		Iterator<String> names = Arrays.asList("name1", "name 2", "name three").iterator();
		Mockito.when(context.getNextLine()).thenAnswer(invocation -> names.next());
		command.execute(context, Arrays.asList());
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setName("name1")),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag().setName("name 2")),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag().setName("name three"))
		));
	}

	@Test
	public void ioExceptionWhenReadingNextLineTerminatesSettingNames() throws IOException {
		Mockito.when(context.getNextLine()).thenThrow(IOException.class);
		command.execute(context, Arrays.asList("New Name"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(
				TaggedFileTest.isTaggedFile("/foo/bar/file1.music", new Tag().setName("New Name")),
				TaggedFileTest.isTaggedFile("/foo/bar/file2.music", new Tag()),
				TaggedFileTest.isTaggedFile("/foo/bar/file3.music", new Tag())
		));
	}

}
