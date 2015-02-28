package net.pterodactylus.ams.core;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A context encapsulates the environment in which {@link Command}s are executed.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Context {

	private final AtomicBoolean shouldExit = new AtomicBoolean();
	private final Session session;
	private final Writer writer;

	public Context(Session session, Writer writer) {
		this.session = session;
		this.writer = writer;
	}

	public void exit() {
		shouldExit.set(true);
	}

	public boolean shouldExit() {
		return shouldExit.get();
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
