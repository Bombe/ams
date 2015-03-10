package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagWriter;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link SaveCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SaveCommandTest {

	private final TagWriter tagWriter = Mockito.mock(TagWriter.class);
	private final SaveCommand command = new SaveCommand(tagWriter);
	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();
	private final Tag tag1 = new Tag().setArtist("Artist").setAlbum("Album").setTrack(1).setName("Name");
	private final Tag tag2 = new Tag().setArtist("Foo").setAlbum("Bar").setTrack(42).setName("Baz");

	@Before
	public void setupSession() throws IOException {
		createFile("/foo/bar/file1.music", tag1);
		createFile("/foo/bar/file2.music", tag2);
	}

	private void createFile(String filename, Tag tag) throws IOException {
		Path file = Paths.get(filename);
		session.addFile(new TaggedFile(file, tag));
	}

	@Test
	public void canCreateDefaultCommand() {
		new SaveCommand();
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.is("save"));
	}

	@Test
	public void allFilesInSessionGetTheirTagsWritten() throws IOException {
		command.execute(context, Collections.<String>emptyList());
		Mockito.verify(tagWriter)
				.write(org.mockito.Matchers.eq(tag1), org.mockito.Matchers.argThat(isFile("/foo/bar/file1.music")));
		Mockito.verify(tagWriter)
				.write(org.mockito.Matchers.eq(tag2), org.mockito.Matchers.argThat(isFile("/foo/bar/file2.music")));
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

}
