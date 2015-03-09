package net.pterodactylus.ams.core;

import java.time.LocalDate;
import java.util.Optional;
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

	@Test
	public void sessionRetainsAddedFiles() {
		TaggedFile file1 = createTaggedFile(new Tag());
		TaggedFile file2 = createTaggedFile(new Tag());
		session.addFile(file1);
		session.addFile(file2);
		MatcherAssert.assertThat(session.getFiles(), Matchers.containsInAnyOrder(file1, file2));
	}

	@Test
	public void filesCanBeRemovedFromSession() {
		TaggedFile file1 = createTaggedFile(new Tag());
		TaggedFile file2 = createTaggedFile(new Tag());
		session.addFile(file1);
		session.addFile(file2);
		session.removeFile(file1);
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(file2));
	}

	private TaggedFile createTaggedFile(Tag tag) {
		return new TaggedFile(Mockito.mock(Path.class), tag);
	}

}
