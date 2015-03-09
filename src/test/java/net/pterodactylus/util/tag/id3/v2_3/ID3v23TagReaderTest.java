package net.pterodactylus.util.tag.id3.v2_3;

import static java.time.LocalDate.ofYearDay;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.TestUtils.createFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import net.pterodactylus.util.tag.Tag;

import org.junit.Test;

/**
 * Unit test for {@link ID3v23TagReader}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v23TagReaderTest {

	private final ID3v23TagReader tagReader = new ID3v23TagReader();

	@Test
	public void canRecognizeId3V23Tags() throws IOException {
		Path file = createFile("files/a/second.id3v2.mp3", getClass());
		Optional<Tag> tag = tagReader.readTags(file);
		assertThat(tag.isPresent(), is(true));
		assertThat(tag.get().getArtist(), is(of("Some Artist")));
		assertThat(tag.get().getAlbum(), is(of("Some Album")));
		assertThat(tag.get().getName(), is(of("Some Song")));
		assertThat(tag.get().getGenre(), is(of("Some Genre")));
		assertThat(tag.get().getTrack(), is(of(4)));
		assertThat(tag.get().getTotalTracks(), is(of(13)));
		assertThat(tag.get().getDisc(), is(of(1)));
		assertThat(tag.get().getTotalDiscs(), is(of(2)));
		assertThat(tag.get().getDate(), is(of(ofYearDay(2014, 1))));
	}

}
