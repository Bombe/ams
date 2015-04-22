package net.pterodactylus.ams.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

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
	private final BufferedReader reader = Mockito.mock(BufferedReader.class);
	private final Context context = Mockito.mock(Context.class);
	private final CommandReader commandReader = new CommandReader(commandDispatcher, reader, context);

	@Test
	public void emptyLineTerminatesCommandReader() throws IOException {
		Mockito.when(reader.readLine()).thenReturn(null);
		commandReader.run();
		Mockito.verify(commandDispatcher, Mockito.never()).runCommand(Matchers.anyString(), Matchers.anyList());
	}

	@Test
	public void ioExceptionTerminatesCommandReader() throws IOException {
		Mockito.when(reader.readLine()).thenThrow(IOException.class);
		commandReader.run();
		Mockito.verify(commandDispatcher, Mockito.never()).runCommand(Matchers.anyString(), Matchers.anyList());
	}

	@Test
	public void emptyLineIsIgnored() throws IOException {
		Mockito.when(reader.readLine()).thenReturn("").thenReturn(null);
		commandReader.run();
		Mockito.verify(commandDispatcher, Mockito.never()).runCommand(Matchers.anyString(), Matchers.anyList());
	}

	@Test
	public void readingALineForwardsToCommandDispatcher() throws IOException {
		Mockito.when(reader.readLine()).thenReturn("test foo bar").thenReturn(null);
		commandReader.run();
		Mockito.verify(commandDispatcher).runCommand(Matchers.eq("test"), Matchers.eq(Arrays.asList("foo", "bar")));
	}

	@Test
	public void runtimeExceptionInCommandDispatcherIsIgnored() throws IOException {
		Mockito.when(reader.readLine()).thenReturn("test foo bar").thenReturn(null);
		Mockito.doThrow(RuntimeException.class)
				.when(commandDispatcher)
				.runCommand(Matchers.anyString(), Matchers.anyList());
		commandReader.run();
		Mockito.verify(commandDispatcher).runCommand(Matchers.eq("test"), Matchers.eq(Arrays.asList("foo", "bar")));
	}

	@Test
	public void addingALinePreventsReadingFromTheReader() throws IOException {
		commandReader.addLine("test foo bar");
		commandReader.run();
		Mockito.verify(commandDispatcher).runCommand(Matchers.eq("test"), Matchers.eq(Arrays.asList("foo", "bar")));
	}

}
