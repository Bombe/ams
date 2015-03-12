package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.core.commands.ConvertCommand.Converter;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;
import net.pterodactylus.util.tag.TaggedFileTest;

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
	private final TaggedFile[] taggedFiles = new TaggedFile[2];
	private final Tag[] tags = new Tag[2];
	private final Path[] sourceFiles = new Path[2];
	private final Path[] tempFiles = new Path[2];
	private final Session session = Mockito.mock(Session.class);
	private final Context context = ContextBuilder.from(session).build();

	@Before
	public void setupSession() {
		createFiles();
		Mockito.when(session.getFiles()).thenReturn(Arrays.asList(taggedFiles[0], taggedFiles[1],
				Mockito.mock(TaggedFile.class)));
	}

	private void createFiles() {
		tags[0] = Mockito.mock(Tag.class);
		tags[1] = Mockito.mock(Tag.class);
		sourceFiles[0] = Mockito.mock(Path.class);
		sourceFiles[1] = Mockito.mock(Path.class);
		tempFiles[0] = Mockito.mock(Path.class);
		tempFiles[1] = Mockito.mock(Path.class);
		taggedFiles[0] = createTaggedFile(sourceFiles[0]);
		Mockito.when(taggedFiles[0].getTag()).thenReturn(tags[0]);
		taggedFiles[1] = createTaggedFile(sourceFiles[1]);
		Mockito.when(taggedFiles[1].getTag()).thenReturn(tags[1]);
		Mockito.when(tempFileSupplier.get()).thenReturn(tempFiles[0], tempFiles[1]);
	}

	private TaggedFile createTaggedFile(Path file) {
		TaggedFile taggedFile = Mockito.mock(TaggedFile.class);
		Mockito.when(taggedFile.getFile()).thenReturn(file);
		return taggedFile;
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
	public void onlySelectedFilesAreFedIntoTheConverter() throws IOException {
		command.execute(context, Arrays.asList("-t", "1,2"));
		Mockito.verify(converter).convert(context, sourceFiles[0], tempFiles[0]);
		Mockito.verify(converter).convert(context, sourceFiles[1], tempFiles[1]);
		Mockito.verifyNoMoreInteractions(converter);
		Mockito.verify(session, Mockito.times(2))
				.replaceFile(org.mockito.Matchers.any(TaggedFile.class), org.mockito.Matchers.any(TaggedFile.class));
		Mockito.verify(session).replaceFile(org.mockito.Matchers.eq(taggedFiles[0]), org.mockito.Matchers.argThat(
				TaggedFileTest.isTaggedFile(tempFiles[0], Optional.of(tags[0]))));
		Mockito.verify(session).replaceFile(org.mockito.Matchers.eq(taggedFiles[1]), org.mockito.Matchers.argThat(
				TaggedFileTest.isTaggedFile(tempFiles[1], Optional.of(tags[1]))));
	}

}
