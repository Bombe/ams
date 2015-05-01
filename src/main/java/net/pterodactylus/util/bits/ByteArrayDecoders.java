package net.pterodactylus.util.bits;

import java.util.function.Function;

/**
 * Helper functions that convert byte arrays into arbitrary values.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ByteArrayDecoders {

	private static final Function<byte[], Integer> INTEGER_32_MSB_FIRST_FUNCTION = value ->
			(value[0] & 0xff) << 24 |
					(value[1] & 0xff) << 16 |
					(value[2] & 0xff) << 8 |
					(value[3] & 0xff);
	private static final Function<byte[], Integer> INTEGER_16_MSB_FIRST_FUNCTION = value ->
			(value[0] & 0xff) << 8 |
					(value[1] & 0xff);

	public static Function<byte[], Integer> integer32BitMsbFirstDecoder() {
		return INTEGER_32_MSB_FIRST_FUNCTION;
	}

	public static Function<byte[], Integer> integer16BitMsbFirstDecoder() {
		return INTEGER_16_MSB_FIRST_FUNCTION;
	}

}
