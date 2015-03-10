package net.pterodactylus.ams.core.commands;

import java.util.function.Predicate;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link CommandUtilsTest}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandUtilsTest {

	@Test
	public void canCreateCommandUtils() {
		new CommandUtils();
	}

	@Test
	public void rangeParserCanParseSingleValues() {
		Predicate<Integer> range = CommandUtils.isInRange("14", 30);
		for (int i = -20; i <= 100; i++) {
			MatcherAssert.assertThat(range.test(i), Matchers.is(i == 14));
		}
	}

	@Test
	public void openRangeMatchesFromLowerBoundToGivenMax() {
		Predicate<Integer> range = CommandUtils.isInRange("14-", 30);
		for (int i = -20; i <= 100; i++) {
			MatcherAssert.assertThat(range.test(i), Matchers.is((i >= 14) && (i <= 30)));
		}
	}

	@Test
	public void closedRangeMatchesFromLowerBoundToUpperBound() {
		Predicate<Integer> range = CommandUtils.isInRange("14-20", 30);
		for (int i = -20; i <= 100; i++) {
			MatcherAssert.assertThat(range.test(i), Matchers.is((i >= 14) && (i <= 20)));
		}
	}

	@Test
	public void multipleRangesMatch() {
		Predicate<Integer> range = CommandUtils.isInRange("1,4-10,15-", 30);
		for (int i = -20; i <= 100; i++) {
			MatcherAssert.assertThat(range.test(i),
					Matchers.is((i == 1) || ((i >= 4) && (i <= 10)) || ((i >= 15) && (i <= 30))));
		}
	}

	@Test
	public void nullRangeMatchesEverything() {
		Predicate<Integer> range = CommandUtils.isInRange(null, 30);
		for (int i = -20; i <= 100; i++) {
			MatcherAssert.assertThat(range.test(i), Matchers.is(true));
		}
	}

}
