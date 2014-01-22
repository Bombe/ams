package net.pterodactylus.ams.commands;

import static java.io.File.listRoots;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.File;
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

	private final ListCommand listCommand = new ListCommand();

	@Test
	public void canDumpFilesFromSessionToOutput() throws IOException {
		StringWriter stringWriter = new StringWriter();
		Session session = createSession(stringWriter);
		listCommand.process(session, emptyList());
		assertThat(stringWriter.toString(), not(isEmptyString()));
	}

	private static Session createSession(StringWriter stringWriter) {
		Session session = new Session();
		session.setOutput(stringWriter);
		session.addFile(new File("src/test/resources/files/second.id3v1.mp3"));
		session.addFile(new File("src/test/resources/files/a/second.id3v2.mp3"));
		session.addFile(new File("src/test/resources/files/a/b/second.vorbis.flac"));
		session.addFile(new File("src/test/resources/files/c/second.vorbis.ogg"));
		return session;
	}

	@Test
	public void listingTakesSingleParameterToFilterListedFiles() throws IOException {
		StringWriter stringWriter = new StringWriter();
		Session session = createSession(stringWriter);
		listCommand.process(session, asList("mp3"));
		assertThat(stringWriter.toString(), containsString("second.id3v1.mp3"));
		assertThat(stringWriter.toString(), containsString("second.id3v2.mp3"));
		assertThat(stringWriter.toString(), not(containsString("second.vorbis.flac")));
		assertThat(stringWriter.toString(), not(containsString("second.vorbis.ogg")));
	}

	@Test
	public void multipleParametersRequireAllParametersToMatch() throws IOException {
		StringWriter stringWriter = new StringWriter();
		Session session = createSession(stringWriter);
		listCommand.process(session, asList("3", "a"));
		assertThat(stringWriter.toString(), not(containsString("second.id3v1.mp3")));
		assertThat(stringWriter.toString(), containsString("second.id3v2.mp3"));
		assertThat(stringWriter.toString(), not(containsString("second.vorbis.flac")));
		assertThat(stringWriter.toString(), not(containsString("second.vorbis.ogg")));
	}

}
