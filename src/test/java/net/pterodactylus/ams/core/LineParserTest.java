package net.pterodactylus.ams.core;

import java.util.Optional;

import net.pterodactylus.ams.core.LineParser.EmptyLine;
import net.pterodactylus.ams.core.LineParser.Result;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link LineParser}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LineParserTest {

	private final LineParser lineParser = new LineParser();

	@Test(expected = EmptyLine.class)
	public void nullThrowsEmptyLine() {
		lineParser.parse(null);
	}

	@Test
	public void emptyLineReturnsEmptyOptional() {
		MatcherAssert.assertThat(lineParser.parse(""), Matchers.is(Optional.<Result>empty()));
	}

	@Test
	public void lineWithWhitespaceOnlyReturnsEmptyOptional() {
		MatcherAssert.assertThat(lineParser.parse("   \t  \t\t "), Matchers.is(Optional.<Result>empty()));
	}

	@Test
	public void singleWordIsRecognizedAsCommandAndEmptyParameters() {
		Result result = lineParser.parse("test").get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test"));
		MatcherAssert.assertThat(result.getParameters(), Matchers.empty());
	}

	@Test
	public void multipleWordsAreRecognizedAsCommandAndParameters() {
		Result result = lineParser.parse("test foo bar").get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test"));
		MatcherAssert.assertThat(result.getParameters(), Matchers.contains("foo", "bar"));
	}

	@Test
	public void backslashCanEscapeWhitespace() {
		Result result = lineParser.parse("test\\ foo").get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test foo"));
		MatcherAssert.assertThat(result.getParameters(), Matchers.empty());
	}

	@Test
	public void singleQuotesCanEscapeWhitespace() {
		Result result = lineParser.parse("te'st 'foo").get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test foo"));
		MatcherAssert.assertThat(result.getParameters(), Matchers.empty());
	}

	@Test
	public void doubleQuotesCanEscapeWhitespaceAndSingleQuotes() {
		Result result = lineParser.parse("te\"st 'fo\"o").get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test 'foo"));
		MatcherAssert.assertThat(result.getParameters(), Matchers.empty());
	}

	@Test
	public void backslashCanEscapeDoubleQuotesWithinDoubleQuotes() {
		Result result = lineParser.parse("te\"st \\\"fo\"o").get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test \"foo"));
		MatcherAssert.assertThat(result.getParameters(), Matchers.empty());
	}

	@Test
	public void multipleWhitespaceBetweenWordsIsCollapsed() {
		Result result = lineParser.parse("test   foo \t\t bar   \t").get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test"));
		MatcherAssert.assertThat(result.getParameters(), Matchers.contains("foo", "bar"));
	}

	@Test
	public void parserCanEscapeStringsWithAllSpecialCharacters() {
		String escaped = lineParser.escape("test \"foo' \\bar");
		Result result = lineParser.parse(escaped).get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test \"foo' \\bar"));
	}

}
