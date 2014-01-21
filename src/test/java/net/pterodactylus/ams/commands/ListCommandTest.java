package net.pterodactylus.ams.commands;

import static java.io.File.listRoots;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;

import java.io.IOException;
import java.io.StringWriter;

import net.pterodactylus.ams.core.Session;

import org.junit.Test;

/**
 * Unit test for {@link ListCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ListCommandTest {

	@Test
	public void canDumpFilesFromSessionToOutput() throws IOException {
		ListCommand listCommand = new ListCommand();
		StringWriter stringWriter = new StringWriter();
		Session session = createSession(stringWriter);
		listCommand.process(session);
		assertThat(stringWriter.toString(), endsWith("End of LIST.\n"));
	}

	private static Session createSession(StringWriter stringWriter) {
		Session session = new Session();
		session.addFile(listRoots()[0]);
		session.setOutput(stringWriter);
		return session;
	}

}
