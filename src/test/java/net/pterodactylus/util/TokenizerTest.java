package net.pterodactylus.util;

import static net.pterodactylus.util.Tokenizer.tokenize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.junit.Test;

/**
 * Unit test for {@link Tokenizer}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TokenizerTest {

	@Test
	public void canTokenizeTwoSimpleWords() {
		assertThat(tokenize("simple words"), contains("simple", "words"));
	}

	@Test
	public void multipleSpacesBetweenTwoWordsAreTreatedAsASingleSpace() {
		assertThat(tokenize("simple   words"), contains("simple", "words"));
	}

	@Test
	public void leadingSpaceDoesNotCreateANewWord() {
		assertThat(tokenize("  simple   words"), contains("simple", "words"));
	}

	@Test
	public void trailingSpaceDoesNotCreateANewWord() {
		assertThat(tokenize("simple   words  "), contains("simple", "words"));
	}

	@Test
	public void wordsWithQuotesAreTokenizedCorrectly() {
		assertThat(tokenize("value \"with space\" found"), contains("value", "with space", "found"));
	}

	@Test
	public void wordsWithEscapeQuotesAreTokenizedCorrectly() {
		assertThat(tokenize("value \"with \\\"space\\\"\" found"), contains("value", "with \"space\"", "found"));
	}

	@Test
	public void wordsWithSingleQuotesAreTokenizedCorrectly() {
		assertThat(tokenize("value 'with space' found"), contains("value", "with space", "found"));
	}

	@Test
	public void wordsWithSingleAndDoubleQuotesAreTokenizedCorrectly() {
		assertThat(tokenize("value 'with \"space\"' found"), contains("value", "with \"space\"", "found"));
	}

	@Test
	public void emptyQuotesWordsAreTokenizedCorrectly() {
		assertThat(tokenize("  '' \"\"    \\    ''"), contains("", "", " ", ""));
	}

	@Test
	public void allThreeKindsOfQuotingInOneWordAreTokenizedCorrectly() {
		assertThat(tokenize("a \"w\"'i'\\ld mix"), contains("a", "wild", "mix"));
	}

	@Test
	public void singleQuotesInDoubleQuotesAreIncluded() {
		assertThat(tokenize("wo\"n't\""), contains("won't"));
	}

}
