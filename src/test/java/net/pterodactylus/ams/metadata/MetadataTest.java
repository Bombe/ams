package net.pterodactylus.ams.metadata;

import static net.pterodactylus.ams.metadata.Metadata.FileType.AUDIO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit test for {@link Metadata}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MetadataTest {

	@Test
	public void metadataStoresType() {
		Metadata metadata = new Metadata(AUDIO);
		assertThat(metadata.getType(), is(AUDIO));
	}

}
