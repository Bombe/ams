package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.core.commands.ConvertCommand.Converter;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link ConvertCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ConvertCommandTest {

	private final Converter converter = Mockito.mock(Converter.class);
	private final Supplier<Path> tempFileSupplier = Mockito.mock(Supplier.class);
	private final ConvertCommand command = new ConvertCommand(converter, tempFileSupplier);
	private final Path[] sourceFiles = new Path[2];
	private final Path[] tempFiles = new Path[2];

	@Before
	public void setupFiles() {
	    sourceFiles[0] = Mockito.mock(Path.class);
	    sourceFiles[1] = Mockito.mock(Path.class);
	    tempFiles[0] = Mockito.mock(Path.class);
	    tempFiles[1] = Mockito.mock(Path.class);
		Mockito.when(tempFileSupplier.get()).thenReturn(tempFiles[0], tempFiles[1]);
	}

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
		Mockito.verify(converter).convert(context, sourceFiles[0], tempFiles[0]);
		Mockito.verify(converter).convert(context, sourceFiles[1], tempFiles[1]);
	}

	private Session createSession() {
		Session session = Mockito.mock(Session.class);
		TaggedFile taggedFile1 = createTaggedFile(sourceFiles[0]);
		TaggedFile taggedFile2 = createTaggedFile(sourceFiles[1]);
		Mockito.when(session.getFiles()).thenReturn(Arrays.asList(taggedFile1, taggedFile2));
		return session;
	}

	private TaggedFile createTaggedFile(Path file) {
		TaggedFile taggedFile = Mockito.mock(TaggedFile.class);
		Mockito.when(taggedFile.getFile()).thenReturn(file);
		return taggedFile;
	}

}
