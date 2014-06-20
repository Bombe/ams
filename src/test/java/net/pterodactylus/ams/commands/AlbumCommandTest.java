package net.pterodactylus.ams.commands;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.TestUtils.createFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.StringWriter;

import net.pterodactylus.ams.core.Session;

import org.junit.Test;

/**
 * Unit test for {@link AlbumCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AlbumCommandTest {

	private final StringWriter stringWriter = new StringWriter();
	private final Session session;
	private final AlbumCommand albumCommand = new AlbumCommand();

	public AlbumCommandTest() throws IOException {
		session = createSession(stringWriter);
	}

	private Session createSession(StringWriter stringWriter) throws IOException {
		Session session = new Session();
		session.addFile(createFile("files/test.unknown", getClass()));
		session.setOutput(stringWriter);
		return session;
	}

	@Test
	public void commandWithoutParameterShowsWhenNoAlbumIsSet() throws IOException {
		albumCommand.process(session, emptyList());
		assertThat(stringWriter.toString(), containsString("No album set."));
	}

	@Test
	public void commandWithoutParameterShowsCurrentAlbum() throws IOException {
		session.setAlbum("Some Album");
		albumCommand.process(session, emptyList());
		assertThat(stringWriter.toString(), containsString("Some Album"));
	}

	@Test
	public void commandWithParametersSetsAlbumName() throws IOException {
		albumCommand.process(session, asList("Some", "Album"));
		assertThat(session.getAlbum(), is(of("Some Album")));
	}

	@Test
	public void unsetAlbumIsShown() throws IOException {
		session.setAlbum(null);
		albumCommand.process(session, emptyList());
		assertThat(stringWriter.toString(), containsString("No album set"));
	}

}
