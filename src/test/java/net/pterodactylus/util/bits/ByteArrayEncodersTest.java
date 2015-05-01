package net.pterodactylus.util.bits;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link ByteArrayEncoders}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ByteArrayEncodersTest {

	@Test
	public void useConstructorForCompleteCoverage() {
	    new ByteArrayEncoders();
	}

	@Test
	public void _16BitIntegerCanBeEncodedMsbFirst() {
		MatcherAssert.assertThat(ByteArrayEncoders.integer16MsbFirstEncoder().apply(0x89ab),
				Matchers.is(new byte[] { (byte) 0x89, (byte) 0xab }));
	}

	@Test
	public void _32BitIntegerCanBeEncodedMsbFirst() {
		MatcherAssert.assertThat(ByteArrayEncoders.integer32MsbFirstEncoder().apply(0x12345678),
				Matchers.is(new byte[] { 0x12, 0x34, 0x56, 0x78 }));
	}

}
