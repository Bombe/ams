package net.pterodactylus.util;

/**
 * Common String utility methods.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StringUtils {

	public static boolean isNullOrEmptyString(String string) {
		return (string == null) || "".equals(trim(string));
	}

	public static String trim(String string) {
		return string.trim();
	}

}
