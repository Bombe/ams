package net.pterodactylus.ams.core;

import static java.util.Collections.unmodifiableCollection;

import java.io.File;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A session binds together the execution of several {@link Command}s and can
 * be used to transport data from one command to another.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Session {

	private Writer writer = createNullWriter();
	private final Set<File> files = new HashSet<>();
	private boolean exit;

	public void addFile(File file) {
		files.add(file);
	}

	public Collection<File> getFiles() {
		return unmodifiableCollection(files);
	}

	public boolean shouldExit() {
		return exit;
	}

	public void exit() {
		exit = true;
	}

	public Writer getOutput() {
		return writer;
	}

	public void setOutput(Writer writer) {
		this.writer = writer;
	}

	private static Writer createNullWriter() {
		return new Writer() {
			@Override
			public void write(char[] buffer, int offset, int length) {
				/* do nothing. */
			}

			@Override
			public void flush() {
				/* do nothing. */
			}

			@Override
			public void close() {
				/* do nothing. */
			}
		};
	}

}
