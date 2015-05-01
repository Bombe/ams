package net.pterodactylus.util.bits;

import java.util.function.Function;

/**
 * Helper functions that convert arbitrary values into byte arrays.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ByteArrayEncoders {

	private static final Function<Integer, byte[]> INTEGER_32_MSB_FIRST_FUNCTION = value -> new byte[] {
			(byte) (value >> 24),
			(byte) (value >> 16),
			(byte) (value >> 8),
			value.byteValue()
	};
	private static final Function<Integer, byte[]> INTEGER_16_MSB_FIRST_FUNCTION = value -> new byte[] {
			(byte) (value >> 8),
			value.byteValue()
	};

	public static Function<Integer, byte[]> integer32MsbFirstEncoder() {
		return INTEGER_32_MSB_FIRST_FUNCTION;
	}

	public static Function<Integer, byte[]> integer16MsbFirstEncoder() {
		return INTEGER_16_MSB_FIRST_FUNCTION;
	}

}
