package net.pterodactylus.util;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

/**
 * Common String utility methods.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StringUtils {

	public static Optional<String> normalize(String string) {
		return isNullOrEmptyString(string) ? empty() : of(trim(string));
	}

	public static boolean isNullOrEmptyString(String string) {
		return (string == null) || "".equals(trim(string));
	}

	public static String trim(String string) {
		return string.trim();
	}

}
