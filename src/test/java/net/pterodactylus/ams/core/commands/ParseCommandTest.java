package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.time.LocalDate;
import java.util.Arrays;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ParseCommandTest}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ParseCommandTest {

	private final Configuration configuration =
			Configuration.unix().toBuilder().setWorkingDirectory("/foo/bar").build();
	private final FileSystem fileSystem = Jimfs.newFileSystem(configuration);
	private final ParseCommand command = new ParseCommand(fileSystem);
	private final Session session = new Session();
	private final Context context = ContextBuilder.from(session).build();
	private final TaggedFile file =
			new TaggedFile(fileSystem.getPath("/foo/bar/2015_supercool/1_the-band_the-song-tag.music"), new Tag());

	@Before
	public void setupSession() {
		session.addFile(file);
	}

	@Test
	public void defaultConstructorCanBeCalled() {
		new ParseCommand();
	}

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(command.getName(), Matchers.is("parse"));
	}

	@Test
	public void filenameIsParsedAccordingToPattern() throws IOException {
		command.execute(context,
				Arrays.asList("-d", "/foo",
						"bar/${Date}_${Album}/${Track}_${Artist}_${Name}-tag.music"));
		MatcherAssert.assertThat(file.getTag().getTrack().get(), Matchers.is(1));
		MatcherAssert.assertThat(file.getTag().getArtist().get(), Matchers.is("the-band"));
		MatcherAssert.assertThat(file.getTag().getName().get(), Matchers.is("the-song"));
		MatcherAssert.assertThat(file.getTag().getAlbum().get(), Matchers.is("supercool"));
		MatcherAssert.assertThat(file.getTag().getDate().get(), Matchers.is(LocalDate.of(2015, 1, 1)));
	}

	@Test
	public void nonMatchingFilesAreIgnored() throws IOException {
		command.execute(context,
				Arrays.asList("-d", "/foo",
						"bar/${Date}_${Album}/${Track}_-_${Artist}_${Name}.music"));
		MatcherAssert.assertThat(file.getTag(), Matchers.is(new Tag()));
	}

	@Test
	public void fileExtensionIsIgnoredIfThatAllowsAFileToMatch() throws IOException {
		command.execute(context,
				Arrays.asList("-d", "/foo",
						"bar/${Date}_${Album}/${Track}_${Artist}_${Name}-tag"));
		MatcherAssert.assertThat(file.getTag().getTrack().get(), Matchers.is(1));
		MatcherAssert.assertThat(file.getTag().getArtist().get(), Matchers.is("the-band"));
		MatcherAssert.assertThat(file.getTag().getName().get(), Matchers.is("the-song"));
		MatcherAssert.assertThat(file.getTag().getAlbum().get(), Matchers.is("supercool"));
		MatcherAssert.assertThat(file.getTag().getDate().get(), Matchers.is(LocalDate.of(2015, 1, 1)));
	}

}
