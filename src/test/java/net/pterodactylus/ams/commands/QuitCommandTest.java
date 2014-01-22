package net.pterodactylus.ams.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import net.pterodactylus.ams.core.Session;

import org.junit.Test;

/**
 * Unit test for {@link QuitCommand}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class QuitCommandTest {

	@Test
	public void quiteCommandSetsExitFlagInSession() {
		QuitCommand quitCommand = new QuitCommand();
		Session session = new Session();
		quitCommand.process(session, null);
		assertThat(session.shouldExit(), is(true));
	}

}
