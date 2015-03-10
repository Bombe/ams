package net.pterodactylus.ams.core.commands;

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

	private ContextBuilder() {
	}

	private ContextBuilder(Options options, Session session, Writer writer) {
		this.options = options;
		this.session = session;
		this.writer = writer;
	}

	public ContextBuilder withOptions(Options options) {
		return new ContextBuilder(options, session, writer);
	}

	public ContextBuilder withSession(Session session) {
		return new ContextBuilder(options, session, writer);
	}

	public ContextBuilder withWriter(Writer writer) {
		return new ContextBuilder(options, session, writer);
	}

	public Context build() {
		return new Context(options, session, writer);
	}

	public static ContextBuilder from(Session session) {
		return new ContextBuilder().withSession(session);
	}

}
