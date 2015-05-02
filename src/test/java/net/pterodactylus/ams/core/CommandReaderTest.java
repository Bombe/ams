package net.pterodactylus.ams.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * Unit test for {@link CommandReader}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CommandReaderTest {

	private final CommandDispatcher commandDispatcher = Mockito.mock(CommandDispatcher.class);
	private final Context context = Mockito.mock(Context.class);
	private final CommandReader commandReader = new CommandReader(commandDispatcher, context);

	@Before
	public void setupContext() {
		AtomicBoolean shouldExit = new AtomicBoolean();
		Mockito.doAnswer(invocation -> {
			shouldExit.set(true);
			return null;
		}).when(context).exit();
		Mockito.when(context.shouldExit()).thenAnswer(invocation -> shouldExit.get());
	}

	@Test
	public void emptyLineTerminatesCommandReader() throws IOException {
		Mockito.when(context.getNextLine(Matchers.anyString())).thenReturn(null);
		commandReader.run();
		Mockito.verify(commandDispatcher, Mockito.never()).runCommand(Matchers.anyString(), Matchers.anyList());
	}

	@Test
	public void ioExceptionTerminatesCommandReader() throws IOException {
		Mockito.when(context.getNextLine(Matchers.anyString())).thenThrow(IOException.class);
		commandReader.run();
		Mockito.verify(commandDispatcher, Mockito.never()).runCommand(Matchers.anyString(), Matchers.anyList());
	}

	@Test
	public void emptyLineIsIgnored() throws IOException {
		Mockito.when(context.getNextLine(Matchers.anyString())).thenReturn("").thenReturn(null);
		commandReader.run();
		Mockito.verify(commandDispatcher, Mockito.never()).runCommand(Matchers.anyString(), Matchers.anyList());
	}

	@Test
	public void readingALineForwardsToCommandDispatcher() throws IOException {
		Mockito.when(context.getNextLine(Matchers.anyString())).thenReturn("test foo bar").thenReturn(null);
		commandReader.run();
		Mockito.verify(commandDispatcher).runCommand(Matchers.eq("test"), Matchers.eq(Arrays.asList("foo", "bar")));
	}

	@Test
	public void runtimeExceptionInCommandDispatcherIsIgnored() throws IOException {
		Mockito.when(context.getNextLine(Matchers.anyString())).thenReturn("test foo bar").thenReturn(null);
		Mockito.doThrow(RuntimeException.class)
				.when(commandDispatcher)
				.runCommand(Matchers.anyString(), Matchers.anyList());
		commandReader.run();
		Mockito.verify(commandDispatcher).runCommand(Matchers.eq("test"), Matchers.eq(Arrays.asList("foo", "bar")));
	}

}
