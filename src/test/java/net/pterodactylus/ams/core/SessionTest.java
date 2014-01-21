package net.pterodactylus.ams.core;

import static java.io.File.listRoots;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link Session}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SessionTest {

	private final Session session = new Session();

	@Test
	public void sessionCanStoreFiles() {
		File root = listRoots()[0];
		session.addFile(root);
		Collection<File> files = session.getFiles();
		assertThat(files, contains(root));
	}

	@Test
	public void newSessionHasADefaultWriter() {
		assertThat(session.getOutput(), notNullValue());
	}

	@Test
	public void sessionStoresGivenWriter() {
		StringWriter stringWriter = new StringWriter();
		session.setOutput(stringWriter);
		assertThat(session.getOutput(), Matchers.<Writer>is(stringWriter));
	}

}
