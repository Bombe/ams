package net.pterodactylus.ams.core;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static net.pterodactylus.util.StringUtils.normalize;
import static net.pterodactylus.util.tag.TagReaders.defaultTagReaders;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagReader;

/**
 * A session binds together the execution of several {@link Command}s and can be
 * used to transport data from one command to another.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Session {

	private final TagReader tagReader = defaultTagReaders();
	private Writer writer = new NullWriter();
	private final SortedSet<File> files = new TreeSet<>();
	private final Map<File, Tag> tags = new HashMap<>();
	private boolean exit;

	public void addFile(File file) {
		files.add(file);
		try {
			tags.put(file, tagReader.readTags(file).orElse(new Tag()));
		} catch (IOException e) {
			tags.put(file, new Tag());
		}
	}

	public Collection<File> getFiles() {
		return getFiles(emptyList());
	}

	public Collection<File> getFiles(List<String> filters) {
		List<Pattern> patterns = filters.stream().map(Pattern::compile).collect(toList());
		return files.stream().filter((file) -> patterns.isEmpty() || patterns.stream().allMatch((pattern) -> pattern.matcher(file.getPath()).find())).collect(Collectors.<File>toList());
	}

	public Optional<String> getAlbum() {
		return getSingleValueOrEmpty(Tag::getAlbum);
	}

	private <T> Optional<T> getSingleValueOrEmpty(Function<Tag, Optional<T>> propertyExtractor) {
		Map<Optional<T>, List<File>> albumFiles = groupFilesByTagProperty(propertyExtractor);
		return (albumFiles.size() == 1) ? albumFiles.keySet().iterator().next() : empty();
	}

	private <T> Map<Optional<T>, List<File>> groupFilesByTagProperty(Function<Tag, Optional<T>> propertyExtractor) {
		return tags.keySet().stream().collect(groupingBy(file -> propertyExtractor.apply(tags.get(file))));
	}

	public void setAlbum(String album) {
		tags.values().stream().forEach(tag -> tag.setAlbum(normalize(album).orElse(null)));
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
		if (this.writer instanceof NullWriter) {
			((NullWriter) this.writer).close();
		}
		this.writer = writer;
	}

	private static class NullWriter extends Writer {

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

	}

}
