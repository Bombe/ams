package net.pterodactylus.ams.commands;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
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
	private final Session session = createSession(stringWriter);
	private final AlbumCommand albumCommand = new AlbumCommand();

	private Session createSession(StringWriter stringWriter) {
		Session session = new Session();
		session.setOutput(stringWriter);
		session.setAlbum("Test Album");
		return session;
	}

	@Test
	public void commandWithoutParameterShowsCurrentAlbum() throws IOException {
		albumCommand.process(session, emptyList());
		assertThat(stringWriter.toString(), containsString("Test Album"));
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
