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
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link CleanCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CleanCommandTest {

	private final CleanCommand command = new CleanCommand();
	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.is("clean"));
	}

	@Test
	public void cleaningCorrectlyCapitalizesAllWords() throws IOException {
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setArtist("the artist")));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setName("song by an ARTIST")));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setAlbum("a name fRoM the ALbum")));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setAlbumArtist("name an album artist of")));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setComment("comment for the album")));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setGenre("THE STYLE THE STUFF GOES BY")));
		command.execute(context, Collections.emptyList());
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()),
				Matchers.contains(
						new Tag().setArtist("The Artist"),
						new Tag().setName("Song by an Artist"),
						new Tag().setAlbum("A Name From the Album"),
						new Tag().setAlbumArtist("Name an Album Artist Of"),
						new Tag().setComment("Comment for the Album"),
						new Tag().setGenre("The Style the Stuff Goes By")
				));
	}

	@Test
	public void cleaningCanBeRestrictedToSelectedFiles() throws IOException {
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setArtist("the artist")));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setName("song by an ARTIST")));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), new Tag().setAlbum("a name fRoM the ALbum")));
		command.execute(context, Arrays.asList("-t", "1-2"));
		MatcherAssert.assertThat(session.getFiles().stream().map(TaggedFile::getTag).collect(Collectors.toList()),
				Matchers.contains(
						new Tag().setArtist("The Artist"),
						new Tag().setName("Song by an Artist"),
						new Tag().setAlbum("a name fRoM the ALbum")
				));
	}

}
