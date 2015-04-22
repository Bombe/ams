package net.pterodactylus.ams.core;

/**
 * Escapes lines in a way that prevents the {@link LineParser} from splitting lines.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LineEscaper {

	public String escape(String argument) {
		return argument.replaceAll("([ \"'\\\\])", "\\\\$1");
	}

}
