package net.pterodactylus.ams.core;

import net.pterodactylus.ams.core.LineParser.Result;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link LineEscaper}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LineEscaperTest {

	private final LineEscaper lineEscaper = new LineEscaper();
	private final LineParser lineParser = new LineParser();

	@Test
	public void parserCanEscapeStringsWithAllSpecialCharacters() {
		String escaped = lineEscaper.escape("test \"foo' \\bar");
		Result result = lineParser.parse(escaped).get();
		MatcherAssert.assertThat(result.getCommand(), Matchers.is("test \"foo' \\bar"));
	}

}
