package net.pterodactylus.ams.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import net.pterodactylus.ams.main.Options;

/**
 * A context encapsulates the environment in which {@link Command}s are executed.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Context {

	private final AtomicBoolean shouldExit = new AtomicBoolean();
	private final Options options;
	private final Session session;
	private final Writer writer;
	private final BufferedReader reader;
	private final Deque<String> additionalLines = new LinkedList<>();

	public Context(Options options, Session session, Writer writer, BufferedReader reader) {
		this.options = options;
		this.session = session;
		this.writer = writer;
		this.reader = reader;
	}

	public void exit() {
		shouldExit.set(true);
	}

	public boolean shouldExit() {
		return shouldExit.get();
	}

	public Options getOptions() {
		return options;
	}

	public Session getSession() {
		return session;
	}

	public void write(String line) throws IOException {
		writer.write(line);
	}

	public void flush() throws IOException {
		writer.flush();
	}

	public String getNextLine() throws IOException {
		synchronized (additionalLines) {
			if (!additionalLines.isEmpty()) {
				return additionalLines.removeFirst();
			}
		}
		write("> ");
		flush();
		return reader.readLine();
	}

	public void addLine(String line) {
		synchronized (additionalLines) {
			additionalLines.addLast(line);
		}
	}

}
