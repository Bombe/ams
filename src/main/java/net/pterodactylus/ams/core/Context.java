package net.pterodactylus.ams.core;

import java.io.IOException;
import java.io.Writer;
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

	public Context(Options options, Session session, Writer writer) {
		this.options = options;
		this.session = session;
		this.writer = writer;
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

}
