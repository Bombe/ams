package net.pterodactylus.ams.core.commands;

import java.io.BufferedReader;
import java.io.StringWriter;
import java.io.Writer;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.main.Options;

/**
 * Builder for a {@link Context} object for tests.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ContextBuilder {

	private Options options = new Options();
	private Session session;
	private Writer writer = new StringWriter();
	private BufferedReader reader;

	private ContextBuilder() {
	}

	public ContextBuilder withOptions(Options options) {
		this.options = options;
		return this;
	}

	public ContextBuilder withSession(Session session) {
		this.session = session;
		return this;
	}

	public ContextBuilder withWriter(Writer writer) {
		this.writer = writer;
		return this;
	}

	public ContextBuilder reading(BufferedReader reader) {
		this.reader = reader;
		return this;
	}

	public Context build() {
		return new Context(options, session, writer, reader);
	}

	public static ContextBuilder from(Session session) {
		return new ContextBuilder().withSession(session);
	}

}
