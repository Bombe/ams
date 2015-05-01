package net.pterodactylus.util.bits;

import static net.pterodactylus.util.bits.ByteArrayDecoders.integer16BitMsbFirstDecoder;
import static net.pterodactylus.util.bits.ByteArrayDecoders.integer32BitMsbFirstDecoder;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link ByteArrayDecoders}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ByteArrayDecodersTest {

	@Test
	public void useConstructorForCompleteCoverage() {
		new ByteArrayDecoders();
	}

	@Test
	public void _16BitIntegerCanBeDecodedMsbFirst() {
		MatcherAssert.assertThat(integer16BitMsbFirstDecoder().apply(new byte[] { (byte) 0xfe, (byte) 0xdc }),
				Matchers.is(0xfedc));
	}

	@Test
	public void _32BitIntegerCanBeDecodedMsbFirst() {
		MatcherAssert.assertThat(
				integer32BitMsbFirstDecoder().apply(new byte[] { (byte) 0xfe, (byte) 0xdc, (byte) 0xba, (byte) 0x98 }),
				Matchers.is(0xfedcba98));
	}

}
