package net.pterodactylus.util;

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
		assertThat(StringUtils.trim("  test  "), is("test"));
		assertThat(StringUtils.trim("\t  test \t"), is("test"));
	}

	@Test
	public void nullValuesAreRecognizedAsNullOrEmpty() {
		assertThat(StringUtils.isNullOrEmptyString(null), is(true));
	}

	@Test
	public void stringsContainingOnlyWhitespaceAreRecognizedAsNullOrEmpty() {
		assertThat(StringUtils.isNullOrEmptyString("  \t  "), is(true));
	}

}
