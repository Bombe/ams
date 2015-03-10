package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.core.commands.ConvertCommand.Converter;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link ConvertCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ConvertCommandTest {

	private final Converter converter = Mockito.mock(Converter.class);
	private final ConvertCommand command = new ConvertCommand(converter);

	@Test
	public void canCreateConverterCommand() {
		new ConvertCommand();
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.equalToIgnoringCase("convert"));
	}

	@Test
	public void allFilesAreFedIntoTheConverter() throws IOException {
		Session session = createSession();
		Context context = ContextBuilder.from(session).build();
		command.execute(context, Collections.<String>emptyList());
	}

	private Session createSession() {
		Session session = Mockito.mock(Session.class);
		TaggedFile taggedFile1 = createTaggedFile();
		TaggedFile taggedFile2 = createTaggedFile();
		Mockito.when(session.getFiles()).thenReturn(Arrays.asList(taggedFile1, taggedFile2));
		return session;
	}

	private TaggedFile createTaggedFile() {
		Path file = Paths.get("file" + ((int) (Math.random() * 1000)));
		TaggedFile taggedFile = Mockito.mock(TaggedFile.class);
		Mockito.when(taggedFile.getFile()).thenReturn(file);
		return taggedFile;
	}

}
