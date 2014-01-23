package net.pterodactylus.ams.metadata;

import static net.pterodactylus.ams.metadata.Metadata.FileType.AUDIO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import org.junit.Test;

/**
 * Unit test for {@link MetadataScanner}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MetadataScannerTest {

	private final MetadataScanner metadataScanner = new MetadataScanner();

	@Test
	public void unknownFilesDoNotHaveMetadata() {
		Optional<Metadata> metadata = metadataScanner.scan("test.unknown");
		assertThat(metadata.isPresent(), is(false));
	}

	@Test
	public void detectThatAnMp3FileIsAnAudioFile() {
		Optional<Metadata> metadata = metadataScanner.scan("test.mp3");
		assertThat(metadata.isPresent(), is(true));
		assertThat(metadata.get().getType(), is(AUDIO));
	}

}
