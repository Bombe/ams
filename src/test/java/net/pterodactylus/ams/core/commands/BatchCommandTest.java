package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
 * Unit test for {@link BatchCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BatchCommandTest {

	private final BatchCommand command = new BatchCommand();
	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();

	@Before
	public void setupSession() {
		IntStream.range(0, 5).forEach(n -> session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag())));
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.is("batch"));
	}

	@Test
	public void commandCanSetSeveralTagsForOneValue() throws IOException {
		command.execute(context, Arrays.asList("--name", "Track 1", "Second Track", "Foo", "Bar"));
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()), Matchers.contains(
				new Tag().setName("Track 1"),
				new Tag().setName("Second Track"),
				new Tag().setName("Foo"),
				new Tag().setName("Bar"),
				new Tag()
		));
	}

	@Test
	public void commandCanSetSeveralTagsForMultipleValues() throws IOException {
		command.execute(context, Arrays.asList("--artist", "Track 1", "Second Track", "Foo", "Bar"));
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()), Matchers.contains(
				new Tag().setArtist("Track 1"),
				new Tag().setArtist("Second Track"),
				new Tag().setArtist("Foo"),
				new Tag().setArtist("Bar"),
				new Tag()
		));
	}

	@Test
	public void commandCanSetTagsForSelectedFiles() throws IOException {
		command.execute(context, Arrays.asList("-t", "3-", "--artist", "Track 1", "Second Track", "Foo", "Bar"));
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()), Matchers.contains(
				new Tag(),
				new Tag(),
				new Tag().setArtist("Track 1"),
				new Tag().setArtist("Second Track"),
				new Tag().setArtist("Foo")
		));
	}

}
