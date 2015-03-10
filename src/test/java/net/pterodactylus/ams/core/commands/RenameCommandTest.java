package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link RenameCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RenameCommandTest {

	private final Configuration configuration =
			Configuration.unix().toBuilder().setWorkingDirectory("/foo/bar").build();
	private final FileSystem fileSystem = Jimfs.newFileSystem(configuration);
	private final RenameCommand command = new RenameCommand(fileSystem);
	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();

	@Before
	public void setupSession() throws IOException {
		createFile("/foo/bar/file1.music", new Tag().setArtist("Artist").setAlbum("Album").setTrack(1).setName("Name"));
		createFile("/foo/bar/file2.music", new Tag().setArtist("Foo").setAlbum("Bar").setTrack(42).setName("Baz"));
	}

	@Test
	public void canCreateDefaultCommand() {
		new RenameCommand();
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.is("rename"));
	}

	@Test
	public void filesAreRenamedAccordingToTheirTags() throws IOException {
		command.execute(context, Arrays.asList("${Artist}/${Album}/${Track}-${Name}.music"));
		verifyFilesAreMoved("/foo/bar");
	}

	private void createFile(String filename, Tag tag) throws IOException {
		Path file = Files.createFile(fileSystem.getPath(filename));
		session.addFile(new TaggedFile(file, tag));
	}

	private void verifyFilesAreMoved(String directory) {
		MatcherAssert.assertThat(Files.exists(fileSystem.getPath(directory + "/file1.music")), Matchers.is(false));
		MatcherAssert.assertThat(Files.exists(fileSystem.getPath(directory + "/file2.music")), Matchers.is(false));
		MatcherAssert.assertThat(Files.exists(fileSystem.getPath(directory + "/Artist/Album/1-Name.music")),
				Matchers.is(true));
		MatcherAssert.assertThat(Files.exists(fileSystem.getPath(directory + "/Foo/Bar/42-Baz.music")),
				Matchers.is(true));
	}

	@Test
	public void filesAreNotMovedIfShortDryRunOptionIsGiven() throws IOException {
		command.execute(context, Arrays.asList("-n", "${Artist}/${Album}/${Track}-${Name}.music"));
		verifyFilesAreNotMoved();
	}

	private void verifyFilesAreNotMoved() {
		MatcherAssert.assertThat(Files.exists(fileSystem.getPath("/foo/bar/file1.music")), Matchers.is(true));
		MatcherAssert.assertThat(Files.exists(fileSystem.getPath("/foo/bar/file2.music")), Matchers.is(true));
		MatcherAssert.assertThat(Files.exists(fileSystem.getPath("/foo/bar/Artist/Album/1-Name.music")),
				Matchers.is(false));
		MatcherAssert.assertThat(Files.exists(fileSystem.getPath("/foo/bar/Foo/Bar/42-Baz.music")), Matchers.is(false));
	}

	@Test
	public void filesAreNotMovedIfLongDryRunOptionIsGiven() throws IOException {
		command.execute(context, Arrays.asList("--dry-run", "${Artist}/${Album}/${Track}-${Name}.music"));
		verifyFilesAreNotMoved();
	}

	@Test
	public void filesAreMovedToDifferentDirectoryWithShortOption() throws IOException {
		command.execute(context, Arrays.asList("-d", "/foo", "${Artist}/${Album}/${Track}-${Name}.music"));
		verifyFilesAreMoved("/foo");
	}

	@Test
	public void filesAreMovedToDifferentDirectoryWithLongOption() throws IOException {
		command.execute(context, Arrays.asList("--directory", "/foo", "${Artist}/${Album}/${Track}-${Name}.music"));
		verifyFilesAreMoved("/foo");
	}

}

