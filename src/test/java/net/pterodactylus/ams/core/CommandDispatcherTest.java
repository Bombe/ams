package net.pterodactylus.ams.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * Unit test for {@link CommandDispatcher}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandDispatcherTest {

	private final Context context = Mockito.mock(Context.class);
	private final CommandDispatcher commandDispatcher = new CommandDispatcher(context);
	private final Command testCommand = createCommand("test");
	private final Command fooCommand = createCommand("foo");
	private final Command barCommand = createCommand("bar");

	private Command createCommand(String name) {
		Command command = Mockito.mock(Command.class);
		Mockito.when(command.getName()).thenReturn(name);
		return command;
	}

	@Before
	public void setupCommandProcessor() {
		commandDispatcher.addCommand(testCommand);
		commandDispatcher.addCommand(fooCommand);
		commandDispatcher.addCommand(barCommand);
	}

	@Test
	public void commandProcessorRunsNoCommandIfCommandCanNotBeFound() throws IOException {
		commandDispatcher.runCommand("stuff", Arrays.asList("bar", "baz"));
		verifyCommandWasNotExecuted(testCommand);
		verifyCommandWasNotExecuted(fooCommand);
		verifyCommandWasNotExecuted(barCommand);
	}

	private void verifyCommandWasExecutedWithParameters(Command command, List<String> parameters) throws IOException {
		Mockito.verify(command).execute(context, parameters);
	}

	private void verifyCommandWasNotExecuted(Command command) throws IOException {
		Mockito.verify(command, Mockito.never()).execute(Matchers.any(Context.class), Matchers.anyList());
	}

	@Test
	public void commandProcessorSelectsCorrectCommand() throws IOException {
		commandDispatcher.runCommand("foo", Arrays.asList("bar", "baz"));
		verifyCommandWasNotExecuted(testCommand);
		verifyCommandWasExecutedWithParameters(fooCommand, Arrays.asList("bar", "baz"));
		verifyCommandWasNotExecuted(barCommand);
	}

}
