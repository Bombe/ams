package net.pterodactylus.util.envopt;

import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link Parser}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ParserTest {

	private final Environment environment = Mockito.mock(Environment.class);
	private final Parser parser = new Parser(environment);

	@Test
	public void parserCanParseEnvironmentIntoOptions() {
		Mockito.when(environment.getValue("foo")).thenReturn(Optional.of("test"));
		TestOptions testOptions = parser.parseEnvironment(TestOptions::new);
		MatcherAssert.assertThat(testOptions.getOptionOne(), Matchers.is("test"));
	}

	@Test
	public void parserIgnoresOptionsWithoutAnnotations() {
		MoreTestOptions moreTestOptions = parser.parseEnvironment(MoreTestOptions::new);
		MatcherAssert.assertThat(moreTestOptions.getOptionOne(), Matchers.nullValue());
	}

	@Test
	public void parserCanAssignToFinalValues() {
		Mockito.when(environment.getValue("foo")).thenReturn(Optional.of("test"));
	    FinalTestOptions finalTestOptions = parser.parseEnvironment(FinalTestOptions::new);
		MatcherAssert.assertThat(finalTestOptions.getOptionOne(), Matchers.is("test"));
	}

	/**
	 * Test class with options used by {@link Parser}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class TestOptions {

		@Option("foo")
		private String optionOne;

		public String getOptionOne() {
			return optionOne;
		}

	}

	/**
	 * Test class with options used by {@link Parser}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class FinalTestOptions {

		@Option("foo")
		private final String optionOne = null;

		public String getOptionOne() {
			return optionOne;
		}

	}

	/**
	 * Test class with options used by {@link Parser}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class MoreTestOptions {

		private String optionOne;

		public String getOptionOne() {
			return optionOne;
		}

	}

}
