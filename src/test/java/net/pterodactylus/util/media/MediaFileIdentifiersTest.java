package net.pterodactylus.util.media;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link MediaFileIdentifiersTest}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MediaFileIdentifiersTest {

	private final MediaFileIdentifier identifier1 = Mockito.mock(MediaFileIdentifier.class);
	private final MediaFileIdentifier identifier2 = Mockito.mock(MediaFileIdentifier.class);
	private final MediaFileIdentifier identifier3 = Mockito.mock(MediaFileIdentifier.class);
	private final MediaFileIdentifier combinedIdentifier =
			MediaFileIdentifiers.combine(identifier1, identifier2, identifier3);
	private final File file = Mockito.mock(File.class);

	@Test
	public void canCreateMediaFileIdentifiers() {
		new MediaFileIdentifiers();
	}

	@Test
	public void combinedMediaFileIdentifiersCallsAllGivenIdentifiersInCaseOfNoMatch() throws IOException {
		MatcherAssert.assertThat(combinedIdentifier.isMediaFile(file), Matchers.is(false));
		Mockito.verify(identifier1).isMediaFile(file);
		Mockito.verify(identifier2).isMediaFile(file);
		Mockito.verify(identifier3).isMediaFile(file);
	}

	@Test(expected = IOException.class)
	public void occuredExceptionsAreReturned() throws IOException {
		Mockito.when(identifier1.isMediaFile(file)).thenThrow(IOException.class);
		Mockito.when(identifier2.isMediaFile(file)).thenThrow(IOException.class);
		Mockito.when(identifier3.isMediaFile(file)).thenThrow(IOException.class);
		try {
			combinedIdentifier.isMediaFile(file);
		} catch (IOException ioe1) {
			Mockito.verify(identifier1).isMediaFile(file);
			Mockito.verify(identifier2).isMediaFile(file);
			Mockito.verify(identifier3).isMediaFile(file);
			MatcherAssert.assertThat(Arrays.asList(ioe1.getSuppressed()), Matchers.contains(
					Matchers.instanceOf(IOException.class),
					Matchers.instanceOf(IOException.class),
					Matchers.instanceOf(IOException.class)
			));
			throw ioe1;
		}
	}

	@Test
	public void defaultIdentifiersAreNotNull() {
		MatcherAssert.assertThat(MediaFileIdentifiers.defaultMediaFileIdentifiers(), Matchers.notNullValue());
	}

}
