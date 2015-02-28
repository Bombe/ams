package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.Collections;

import net.pterodactylus.ams.core.Context;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link QuitCommandTest}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class QuitCommandTest {

	private final QuitCommand quitCommand = new QuitCommand();

	@Test
	public void commandReturnsCorrectName() {
		MatcherAssert.assertThat(quitCommand.getName(), Matchers.is("quit"));
	}

	@Test
	public void quitCommandTriggersExitOnContext() throws IOException {
		Context context = Mockito.mock(Context.class);
		quitCommand.execute(context, Collections.<String>emptyList());
		Mockito.verify(context).exit();
	}

}
