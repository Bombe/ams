package net.pterodactylus.ams.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link SelectedFiles}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SelectedFilesTest {

	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();
	private final List<TaggedFile> files = new ArrayList<>();

	@Before
	public void setupSession() {
		IntStream.range(0, 6).forEach(n -> files.add(Mockito.mock(TaggedFile.class)));
		files.stream().forEach(session::addFile);
	}

	@Test
	public void omittingTrackSelectionSelectsAllTracks() {
		SelectedFiles selectedFiles = SelectedFiles.createFrom(context, Collections.<String>emptyList());
		MatcherAssert.assertThat(selectedFiles.getSelectedFiles(), Matchers.is(files));
	}

	@Test
	public void omittingTrackSelectionReturnsAllRemainingParameters() {
		SelectedFiles selectedFiles = SelectedFiles.createFrom(context, Arrays.asList("more", "--param", "eters"));
		MatcherAssert.assertThat(selectedFiles.getParameters(), Matchers.contains("more", "--param", "eters"));
	}

	@Test
	public void singleTrackSelectionSelectsCorrectFiles() {
		SelectedFiles selectedFiles =
				SelectedFiles.createFrom(context, Arrays.asList("-t", "4", "more", "--param", "eters"));
		MatcherAssert.assertThat(selectedFiles.getSelectedFiles(), Matchers.contains(files.get(3)));
		MatcherAssert.assertThat(selectedFiles.getParameters(), Matchers.contains("more", "--param", "eters"));
	}

	@Test
	public void singleRangeSelectionSelectsCorrectFiles() {
		SelectedFiles selectedFiles = SelectedFiles.createFrom(context, Arrays.asList("-t", "2-4"));
		MatcherAssert.assertThat(selectedFiles.getSelectedFiles(),
				Matchers.contains(files.get(1), files.get(2), files.get(3)));
	}

	@Test
	public void multipleRangeSelectionSelectsCorrectFiles() {
		SelectedFiles selectedFiles = SelectedFiles.createFrom(context, Arrays.asList("-t", "1-3,5-6"));
		MatcherAssert.assertThat(selectedFiles.getSelectedFiles(),
				Matchers.contains(files.get(0), files.get(1), files.get(2), files.get(4), files.get(5)));
	}

	@Test
	public void rangeWithoutEndSelectsAllFilesFromTheStartOfTheRangeToTheEndOfTheFiles() {
		SelectedFiles selectedFiles = SelectedFiles.createFrom(context, Arrays.asList("-t", "4-"));
		MatcherAssert.assertThat(selectedFiles.getSelectedFiles(),
				Matchers.contains(files.get(3), files.get(4), files.get(5)));
	}

}
