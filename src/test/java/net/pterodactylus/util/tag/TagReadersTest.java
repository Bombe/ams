package net.pterodactylus.util.tag;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.TagReaders.combine;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.junit.Test;

/**
 * Unit test for {@link TagReaders}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TagReadersTest {

	private final Tag tag = new Tag();
	private final TagReader matchingTagReader = mock(TagReader.class);
	private final TagReader nonMatchingTagReader = mock(TagReader.class);

	public TagReadersTest() throws IOException {
		when(matchingTagReader.readTags(any(File.class))).thenReturn(of(tag));
		when(nonMatchingTagReader.readTags(any(File.class))).thenReturn(empty());
	}

	@Test
	public void tagReadersCanBeCreatedToIncreaseCoverage() {
	    new TagReaders();
	}

	@Test
	public void firstMatchingTagReaderTerminatesTagReading() throws IOException {
		TagReader combinedTagReaders = combine(matchingTagReader, nonMatchingTagReader);
		Optional<Tag> readTag = combinedTagReaders.readTags(mock(File.class));
		assertThat(readTag, is(of(tag)));
		verify(matchingTagReader).readTags(any(File.class));
		verify(nonMatchingTagReader, never()).readTags(any(File.class));
	}

	@Test
	public void allTagReadersAreTried() throws IOException {
		TagReader combinedTagReaders = combine(nonMatchingTagReader, matchingTagReader);
		Optional<Tag> readTag = combinedTagReaders.readTags(mock(File.class));
		assertThat(readTag, is(of(tag)));
		verify(nonMatchingTagReader).readTags(any(File.class));
		verify(matchingTagReader).readTags(any(File.class));
	}

}
