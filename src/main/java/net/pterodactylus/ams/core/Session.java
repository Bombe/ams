package net.pterodactylus.ams.core;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A session binds together the execution of several {@link Command}s and can
 * be used to transport data from one command to another.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Session {

	private Writer writer = createNullWriter();
	private final SortedSet<File> files = new TreeSet<>();
	private boolean exit;
	private Optional<String> album = empty();

	public void addFile(File file) {
		files.add(file);
	}

	public Collection<File> getFiles() {
		return getFiles(emptyList());
	}

	public Collection<File> getFiles(List<String> filters) {
		List<Pattern> patterns = filters.stream().map(Pattern::compile).collect(toList());
		return files.stream().filter((file) -> patterns.isEmpty() || patterns.stream().allMatch((pattern) -> pattern.matcher(file.getPath()).find())).collect(Collectors.<File>toList());
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

	public Optional<String> getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = isNullOrEmptyString(album) ? empty() : of(trim(album));
	}

	private boolean isNullOrEmptyString(String album) {
		return (album == null) || "".equals(trim(album));
	}

	private String trim(String album) {
		return album.trim();
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
