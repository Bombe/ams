package net.pterodactylus.ams.commands;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import net.pterodactylus.ams.core.Session;

import org.junit.Test;

/**
 * Unit test for {@link ScanCommand}
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ScanCommandTest {

	private final ScanCommand scanCommand = new ScanCommand();

	@Test
	public void scanDoesNotShowUnknownFiles() throws IOException {
		StringWriter stringWriter = new StringWriter();
		Session session = new Session();
		session.setOutput(stringWriter);
		session.addFile(new File("test.unknown"));
		scanCommand.process(session, emptyList());
		assertThat(stringWriter.toString(), not(containsString("test.unknown")));
	}

	@Test
	public void scanIdentifiesAudioFiles() throws IOException {
		StringWriter stringWriter = new StringWriter();
		Session session = new Session();
		session.setOutput(stringWriter);
		session.addFile(new File("test.mp3"));
		scanCommand.process(session, emptyList());
		assertThat(stringWriter.toString(), containsString("test.mp3"));
		assertThat(stringWriter.toString(), containsString("AUDIO:"));
	}

}
