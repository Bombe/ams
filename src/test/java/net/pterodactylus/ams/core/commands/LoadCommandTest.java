package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.media.MediaFileIdentifier;
import net.pterodactylus.util.tag.TaggedFile;
import net.pterodactylus.util.tag.TaggedFileTest;

import com.google.common.jimfs.Jimfs;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link LoadCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LoadCommandTest {

	private final FileSystem fileSystem = Jimfs.newFileSystem();
	private final MediaFileIdentifier mediaFileIdentifier = Mockito.mock(MediaFileIdentifier.class);
	private final LoadCommand command = new LoadCommand(fileSystem, mediaFileIdentifier);
	private final Session session = Mockito.mock(Session.class);
	private final Context context = ContextBuilder.from(session).build();

	@Test
	public void canCreateDefaultCommand() {
		new LoadCommand();
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.equalToIgnoringCase("load"));
	}

	@Test
	public void singleMediaFileIsAddedToSession() throws IOException {
		createMediaFile();
		command.execute(context, Arrays.asList("/foo/bar/media-file"));
		verifyMediaFileIsAdded();
	}

	private void verifyMediaFileIsAdded() {
		Mockito.verify(session).addFile(org.mockito.Matchers.argThat(TaggedFileTest.isTaggedFile("/foo/bar/media-file")));
	}

	private void createMediaFile() throws IOException {
		Path directoryPath = createBasePath();
		Path filePath = directoryPath.resolve("media-file");
		Files.createFile(filePath);
		Mockito.when(mediaFileIdentifier.isMediaFile(org.mockito.Matchers.argThat(isFile("/foo/bar/media-file"))))
				.thenReturn(true);
	}

	private Path createBasePath() throws IOException {
		Path directoryPath = fileSystem.getPath("/foo", "bar");
		Files.createDirectories(directoryPath);
		return directoryPath;
	}

	private Matcher<Path> isFile(String filename) {
		return new TypeSafeDiagnosingMatcher<Path>() {
			@Override
			protected boolean matchesSafely(Path file, Description mismatchDescription) {
				if (!file.toString().equals(filename)) {
					mismatchDescription.appendText("file is at ").appendValue(file.toString());
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("is file at ").appendValue(filename);
			}
		};
	}

	@Test
	public void singleNonMediaFileIsNotAddedToSession() throws IOException {
		createNonMediaFile();
		command.execute(context, Arrays.asList("/foo/bar/non-media-file"));
		verifyNoFileIsAdded();
	}

	private void createNonMediaFile() throws IOException {
		Path directoryPath = createBasePath();
		Path filePath = directoryPath.resolve("non-media-file");
		Files.createFile(filePath);
	}

	@Test
	public void directoryIsScannedAndMediaFilesAreAddedToSession() throws IOException {
		createMediaFile();
		createNonMediaFile();
		command.execute(context, Arrays.asList("/foo/bar"));
		verifyMediaFileIsAdded();
	}

	private void verifyNoFileIsAdded() {
		Mockito.verify(session, Mockito.never()).addFile(org.mockito.Matchers.any(TaggedFile.class));
	}

	@Test
	public void nonMatchingWildcardDoesNotAddAnyFiles() throws IOException {
		createMediaFile();
		createNonMediaFile();
		command.execute(context, Arrays.asList("/foo/bar*/g*"));
		verifyNoFileIsAdded();
	}

	@Test
	public void matchingWildcardAddsMediaFiles() throws IOException {
		createMediaFile();
		createNonMediaFile();
		command.execute(context, Arrays.asList("/foo/bar**"));
		verifyMediaFileIsAdded();
	}

	@Test
	public void invalidFilesWithoutWildcardsAreSkipped() throws IOException {
		command.execute(context, Arrays.asList("/foo/bar/missing"));
		verifyNoFileIsAdded();
	}

}
