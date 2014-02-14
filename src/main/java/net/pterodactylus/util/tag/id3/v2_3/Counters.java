package net.pterodactylus.util.tag.id3.v2_3;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulates parsing a pair of counters, e.g. for the current track and the
 * total number of tracks.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class Counters {

	private static final Pattern countersPattern = compile("(\\d+)?\\s*(/\\s*(\\d+)?)?");
	private final int current;
	private final int total;

	private Counters(int current, int total) {
		this.current = current;
		this.total = total;
	}

	public int getCurrent() {
		return current;
	}

	public int getTotal() {
		return total;
	}

	public static Optional<Counters> parseCounters(String text) {
		Matcher matcher = countersPattern.matcher(text);
		if (!matcher.matches()) {
			return empty();
		}
		int current = ofNullable(matcher.group(1)).map(Integer::valueOf).orElse(0);
		int total = ofNullable(matcher.group(3)).map(Integer::valueOf).orElse(0);
		return of(new Counters(current, total));
	}

}
