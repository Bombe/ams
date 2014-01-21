package net.pterodactylus.ams.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandProcessorTest {

	@Test
	public void canCallCommandWithSession() {
		Command command = mock(Command.class);
		Session session = new Session();
		CommandProcessor commandProcessor = new CommandProcessor(session);
		commandProcessor.process(command);
		verify(command).process(session);
	}

}
