package net.pterodactylus.ams.core;

import java.nio.file.Path;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link Session}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SessionTest {

	private final Session session = new Session();
	private final TaggedFile file1 = new TaggedFile(Mockito.mock(Path.class), new Tag());
	private final TaggedFile file2 = new TaggedFile(Mockito.mock(Path.class), new Tag());
	private final TaggedFile file3 = new TaggedFile(Mockito.mock(Path.class), new Tag());

	@Test
	public void sessionRetainsAddedFiles() {
		session.addFile(file1);
		session.addFile(file2);
		MatcherAssert.assertThat(session.getFiles(), Matchers.containsInAnyOrder(file1, file2));
	}

	@Test
	public void filesCanBeRemovedFromSession() {
		session.addFile(file1);
		session.addFile(file2);
		session.removeFile(file1);
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(file2));
	}

	@Test
	public void filesCanBeReplacedInSession() {
		session.addFile(file1);
		session.addFile(file2);
		session.replaceFile(file1, file3);
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(file3, file2));
	}

}
