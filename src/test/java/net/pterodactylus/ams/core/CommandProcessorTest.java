package net.pterodactylus.ams.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

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
		commandProcessor.process(command, emptyList());
		verify(command).process(session, emptyList());
	}

	@Test
	public void canForwardArgumentsToCommand() throws IOException {
		Command command = mock(Command.class);
		when(command.getName()).thenReturn("Test");
		Session session = new Session();
		CommandProcessor commandProcessor = new CommandProcessor(session);
		List<String> arguments = asList("some", "argument", "here");
		commandProcessor.process(command, arguments);
		verify(command).process(session, arguments);
	}


}
