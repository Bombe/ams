package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link RemoveCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RemoveCommandTest {

	private final RemoveCommand removeCommand = new RemoveCommand();
	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(removeCommand.getName(), Matchers.is("remove"));
	}
	
	@Test
	public void commandRemovesSelectedFiles() throws IOException {
		IntStream.range(0, 3).forEach(n -> session.addFile(Mockito.mock(TaggedFile.class)));
		TaggedFile lastFile = Mockito.mock(TaggedFile.class);
		session.addFile(lastFile);
		removeCommand.execute(context, Arrays.asList("-t", "1-3"));
		MatcherAssert.assertThat(session.getFiles(), Matchers.contains(lastFile));
	}

}
