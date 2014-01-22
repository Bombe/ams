package net.pterodactylus.ams.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link CommandDispatcher}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandDispatcherTest {

	private final CommandProcessor commandProcessor = mock(CommandProcessor.class);
	private final CommandDispatcher commandDispatcher = new CommandDispatcher(commandProcessor);

	@Before
	public void setup() throws IOException {
		doAnswer((invocation) -> {
			((Command) invocation.getArguments()[0]).process(null, (List<String>) invocation.getArguments()[1]);
			return null;
		}).when(commandProcessor).process(any(), any());
	}

	@Test
	public void canAddCommandToDispatcher() {
		Command command = createCommand("Test");
		commandDispatcher.addCommand(command);
	}

	@Test
	public void noDispatchIfNoArgumentsAreGiven() throws IOException {
		Command firstCommand = createCommand("first");
		Command secondCommand = createCommand("second");
		commandDispatcher.addCommand(firstCommand);
		commandDispatcher.addCommand(secondCommand);
		commandDispatcher.dispatch("");
		verify(firstCommand, never()).process(any(), any());
		verify(secondCommand, never()).process(any(), any());
	}

	@Test
	public void canDispatchACommand() throws IOException {
		Command firstCommand = createCommand("first");
		Command secondCommand = createCommand("second");
		commandDispatcher.addCommand(firstCommand);
		commandDispatcher.addCommand(secondCommand);
		commandDispatcher.dispatch("first");
		verify(firstCommand).process(any(), any());
		verify(secondCommand, never()).process(any(),any() );
	}

	@Test
	public void canDispatchACommandWithParameters() throws IOException {
		Command firstCommand = createCommand("first");
		Command secondCommand = createCommand("second");
		commandDispatcher.addCommand(firstCommand);
		commandDispatcher.addCommand(secondCommand);
		commandDispatcher.dispatch("first with parameter");
		verify(firstCommand).process(any(), eq(asList("with", "parameter")));
		verify(secondCommand, never()).process(any(),eq(emptyList()));
	}

	@Test
	public void doesNotDispatchANonExistingCommand() throws IOException {
		Command firstCommand = createCommand("first");
		Command secondCommand = createCommand("second");
		commandDispatcher.addCommand(firstCommand);
		commandDispatcher.addCommand(secondCommand);
		commandDispatcher.dispatch("third");
		verify(firstCommand, never()).process(any(), any());
		verify(secondCommand, never()).process(any(), any());
	}

	private Command createCommand(String name) {
		Command command = mock(Command.class);
		when(command.getName()).thenReturn(name);
		return command;
	}


}
