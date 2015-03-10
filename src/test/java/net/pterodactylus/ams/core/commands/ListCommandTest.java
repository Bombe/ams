package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link ListCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ListCommandTest {

	private final ListCommand command = new ListCommand();
	private final StringWriter writer = new StringWriter();
	private final Tag fullTag = new Tag();
	private final Tag tagWithoutTotals = new Tag();
	private final Tag emptyTag = new Tag();

	@Before
	public void setupTag1() {
		fullTag.setAlbum("Album1");
		fullTag.setAlbumArtist("AlbumArtist1");
		fullTag.setArtist("Artist1");
		fullTag.setComment("Comment1");
		fullTag.setDate(LocalDate.of(2011, 1, 1));
		fullTag.setDisc(1);
		fullTag.setGenre("Genre1");
		fullTag.setName("Name1");
		fullTag.setTotalDiscs(2);
		fullTag.setTotalTracks(21);
		fullTag.setTrack(11);
	}

	@Before
	public void setupTag2() {
		tagWithoutTotals.setAlbum("Album2");
		tagWithoutTotals.setAlbumArtist("AlbumArtist2");
		tagWithoutTotals.setArtist("Artist2");
		tagWithoutTotals.setComment("Comment2");
		tagWithoutTotals.setDate(LocalDate.of(2022, 2, 2));
		tagWithoutTotals.setDisc(2);
		tagWithoutTotals.setGenre("Genre2");
		tagWithoutTotals.setName("Name2");
		tagWithoutTotals.setTrack(12);
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.equalToIgnoringCase("list"));
	}

	@Test
	public void commandListsFilesInSession() throws IOException {
		Context context = createContext();
		command.execute(context, Collections.emptyList());
		List<String> lines = Arrays.asList(writer.toString().split("\n"));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Album", "Album1")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Album Artist", "AlbumArtist1")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Artist", "Artist1")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Comment", "Comment1")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Date", "2011-01-01")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Disc", "1 / 2")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Genre", "Genre")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Name", "Name1")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Track", "11 / 21")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Album", "Album2")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Album Artist", "AlbumArtist2")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Artist", "Artist2")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Comment", "Comment2")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Date", "2022-02-02")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Disc", "2")));
		MatcherAssert.assertThat(lines, Matchers.not(Matchers.hasItem(matchLine("Disc", "2 /"))));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Genre", "Genre")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Name", "Name2")));
		MatcherAssert.assertThat(lines, Matchers.hasItem(matchLine("Track", "12")));
		MatcherAssert.assertThat(lines, Matchers.not(Matchers.hasItem(matchLine("Track", "12 /"))));
	}

	private Matcher<String> matchLine(String title, String value) {
		return Matchers.allOf(Matchers.containsString("  " + title + ": "), containsFiltered(value));
	}

	private Matcher<? super String> containsFiltered(String value) {
		return new TypeSafeDiagnosingMatcher<String>() {
			@Override
			protected boolean matchesSafely(String text, Description mismatchDescription) {
				text = text.replaceAll("\u001b\\[[^m]*m", "");
				return text.contains(value);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("contains ").appendValue(value);
			}
		};
	}

	private Context createContext() {
		Session session = createSession();
		return ContextBuilder.from(session).withWriter(writer).build();
	}

	private Session createSession() {
		Session session = new Session();
		session.addFile(new TaggedFile(Mockito.mock(Path.class), fullTag));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), tagWithoutTotals));
		session.addFile(new TaggedFile(Mockito.mock(Path.class), emptyTag));
		return session;
	}

}
