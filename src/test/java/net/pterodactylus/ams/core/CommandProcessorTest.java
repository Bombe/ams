package net.pterodactylus.ams.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandProcessorTest {

	@Test
	public void canCallCommandWithSession() throws IOException {
		Command command = mock(Command.class);
		when(command.getName()).thenReturn("Test");
		Session session = new Session();
		CommandProcessor commandProcessor = new CommandProcessor(session);
		commandProcessor.process(command);
		verify(command).process(session);
	}

}
