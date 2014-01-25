package net.pterodactylus.util;

import static net.pterodactylus.util.StringUtils.isNullOrEmptyString;
import static net.pterodactylus.util.StringUtils.trim;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

}
