package net.pterodactylus.ams.core.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.main.Options;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link ContextBuilder}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ContextBuilderTest {

	private final Options options = Mockito.mock(Options.class);
	private final Session session = Mockito.mock(Session.class);
	private final Writer writer = Mockito.mock(Writer.class);
	private final BufferedReader reader = Mockito.mock(BufferedReader.class);
	private final ContextBuilder contextBuilder = ContextBuilder.from(session);

	@Test
	public void contextContainsCorrectSession() {
		MatcherAssert.assertThat(contextBuilder.build().getSession(), Matchers.is(session));
	}

	@Test
	public void contextAlwaysContainsOptions() {
		Context context = contextBuilder.build();
		MatcherAssert.assertThat(context.getOptions(), Matchers.notNullValue());
		MatcherAssert.assertThat(context.getOptions(), Matchers.not(Matchers.is(options)));
	}

	@Test
	public void contextRetainsOptions() {
		contextBuilder.withOptions(options);
		MatcherAssert.assertThat(contextBuilder.build().getOptions(), Matchers.is(options));
	}

	@Test
	public void contextAlwaysContainsWriter() throws IOException {
		contextBuilder.build().write("test");
	}

	@Test
	public void contextRetainsWriter() throws IOException {
		contextBuilder.withWriter(writer);
		contextBuilder.build().write("test");
		Mockito.verify(writer).write("test");
	}

	@Test
	public void contextRetainsReader() throws IOException {
		contextBuilder.reading(reader);
		contextBuilder.build().getNextLine("> ");
		Mockito.verify(reader).readLine();
	}

}
