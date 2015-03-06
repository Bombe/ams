package net.pterodactylus.ams.core;

import java.io.IOException;
import java.io.Writer;

import net.pterodactylus.ams.main.Options;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link ContextTest}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ContextTest {

	private final Options options = Mockito.mock(Options.class);
	private final Session session = Mockito.mock(Session.class);
	private final Writer writer = Mockito.mock(Writer.class);
	private final Context context = new Context(options, session, writer);

	@Test
	public void newContextShouldNotExit() {
		MatcherAssert.assertThat(context.shouldExit(), Matchers.is(false));
	}

	@Test
	public void contextStoresExitFlag() {
		context.exit();
		MatcherAssert.assertThat(context.shouldExit(), Matchers.is(true));
	}

	@Test
	public void contextRetainsSession() {
		MatcherAssert.assertThat(context.getSession(), Matchers.is(session));
	}

	@Test
	public void contextWritesLinesToWriter() throws IOException {
		context.write("A test line.");
		Mockito.verify(writer).write("A test line.");
	}

	@Test
	public void contextCanFlushWriter() throws IOException {
		context.flush();
		Mockito.verify(writer).flush();
	}

}
