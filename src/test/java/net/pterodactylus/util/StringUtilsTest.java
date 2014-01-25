package net.pterodactylus.util;

import static java.util.Optional.of;
import static net.pterodactylus.util.StringUtils.isNullOrEmptyString;
import static net.pterodactylus.util.StringUtils.normalize;
import static net.pterodactylus.util.StringUtils.trim;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import org.junit.Test;

/**
 * Unit test for {@link StringUtils}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StringUtilsTest {

	@Test
	public void trimRemovesWhitespaceAtBeginningAndEndOfString() {
		assertThat(trim("  test  "), is("test"));
		assertThat(trim("\t  test \t"), is("test"));
	}

	@Test
	public void nullValuesAreRecognizedAsNullOrEmpty() {
		assertThat(isNullOrEmptyString(null), is(true));
	}

	@Test
	public void stringsContainingOnlyWhitespaceAreRecognizedAsNullOrEmpty() {
		assertThat(isNullOrEmptyString("  \t  "), is(true));
	}

	@Test
	public void nullStringsAreNormalizedToEmpty() {
		assertThat(normalize(null), is(Optional.<String>empty()));
	}

	@Test
	public void emptyStringsAreNormalizedToEmpty() {
		assertThat(normalize("  \t  "), is(Optional.<String>empty()));
	}

	@Test
	public void nonEmptyStringsAreNormalizedToTheTrimmedValue() {
		assertThat(normalize("  test  \t"), is(of("test")));
	}

}
