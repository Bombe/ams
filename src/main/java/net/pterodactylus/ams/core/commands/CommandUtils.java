package net.pterodactylus.ams.core.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Helper methods for {@link Command} implementations.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandUtils {

	public static Predicate<Integer> isInRange(String range, int maxCount) {
		if (range == null) {
			return i -> true;
		}
		Set<Integer> containedIndices = new HashSet<>();
		Arrays.asList(range.split(",")).stream()
				.forEach(singleRange -> {
							if (singleRange.endsWith("-")) {
								int lowerBound = Integer.valueOf(singleRange.substring(0, singleRange.indexOf('-')));
								IntStream.rangeClosed(lowerBound, maxCount).forEach(containedIndices::add);
							} else if (singleRange.contains("-")) {
								int lowerBound = Integer.valueOf(singleRange.substring(0, singleRange.indexOf('-')));
								int upperBound = Integer.valueOf(singleRange.substring(singleRange.indexOf('-') + 1));
								IntStream.rangeClosed(lowerBound, upperBound).forEach(containedIndices::add);
							} else {
								containedIndices.add(Integer.valueOf(singleRange));
							}
						}
				);
		return index -> containedIndices.contains(index);
	}

}
