package net.pterodactylus.util.tag.id3.v2_3;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link Unsynchronizer}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class UnsynchronizerTest {

	private final Unsynchronizer unsynchronizer = new Unsynchronizer();

	@Test
	public void tooShortArrayWillNotRequireUnsynchronization() {
		byte[] data = { (byte) 0xff };
		MatcherAssert.assertThat(unsynchronizer.isUnsychronizationRequired(data), Matchers.is(false));
	}

	@Test
	public void needForUnsynchronizationIsRecognized() {
		byte[] data = { 0x00, (byte) 0xff, (byte) 0xff, 0x00 };
		MatcherAssert.assertThat(unsynchronizer.isUnsychronizationRequired(data), Matchers.is(true));
	}

	@Test
	public void missingNeedForUnsychronizationIsRecognized() {
		byte[] data = { 0x00, (byte) 0xff, (byte) 0x7f, 0x00 };
		MatcherAssert.assertThat(unsynchronizer.isUnsychronizationRequired(data), Matchers.is(false));
	}

	@Test
	public void unsynchronizationIsPerformedCorrectlyWhenSyncBytesPresent() {
		byte[] data = { 0x00, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0xff, (byte) 0xe4 };
		byte[] expectedData = { 0x00, (byte) 0xff, 0x00, (byte) 0xff, 0x00, 0x00, (byte) 0xff, 0x00, (byte) 0xe4 };
		MatcherAssert.assertThat(unsynchronizer.unsynchronize(data), Matchers.is(expectedData));
	}

	@Test
	public void deunsynchronizedIsPerformedCorrectly() {
		byte[] data = { 0x00, (byte) 0xff, 0x01, (byte) 0xff, 0x00, (byte) 0xff, 0x00, (byte) 0xff };
		byte[] expectedData = { 0x00, (byte) 0xff, 0x01, (byte) 0xff, (byte) 0x0ff, (byte) 0xff };
		MatcherAssert.assertThat(unsynchronizer.deunsynchronize(data), Matchers.is(expectedData));
	}

}
