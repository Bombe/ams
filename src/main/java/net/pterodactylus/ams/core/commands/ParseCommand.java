package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.annotations.VisibleForTesting;

/**
 * Parses information from the filename into the tag.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ParseCommand extends AbstractCommand {

	private final FileSystem fileSystem;

	public ParseCommand() {
		this(FileSystems.getDefault());
	}

	@VisibleForTesting
	ParseCommand(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	@Override
	public String getName() {
		return "parse";
	}

	@Override
	protected void executeForFiles(Context context, List<TaggedFile> selectedFiles, List<String> parameters)
	throws IOException {
		Options options = new Options();
		new JCommander(options).parse(parameters.toArray(new String[parameters.size()]));
		Pattern pattern = createPattern(options.patternValues.stream().collect(Collectors.joining(" ")));
		Path baseDirectory = fileSystem.getPath(options.baseDirectory).toAbsolutePath();
		for (TaggedFile taggedFile : selectedFiles) {
			String relativePath = baseDirectory.relativize(taggedFile.getFile().toAbsolutePath().normalize()).toString();
			Matcher matcher = pattern.matcher(relativePath);
			if (!matcher.matches()) {
				if (relativePath.lastIndexOf('.') > relativePath.lastIndexOf('/')) {
					relativePath = relativePath.substring(0, relativePath.lastIndexOf('.'));
				}
				matcher = pattern.matcher(relativePath);
				if (!matcher.matches()) {
					continue;
				}
			}
			getValueFromMatcher(taggedFile, matcher, "Track", (tag, track) -> tag.setTrack(track), Integer::valueOf);
			getValueFromMatcher(taggedFile, matcher, "Artist", (tag, artist) -> tag.setArtist(artist), Function.identity());
			getValueFromMatcher(taggedFile, matcher, "AlbumArtist", (tag, albumArtist) -> tag.setAlbumArtist(albumArtist), Function.identity());
			getValueFromMatcher(taggedFile, matcher, "Name", (tag, name) -> tag.setName(name), Function.identity());
			getValueFromMatcher(taggedFile, matcher, "Album", (tag, album) -> tag.setAlbum(album), Function.identity());
			getValueFromMatcher(taggedFile, matcher, "Disc", (tag, disc) -> tag.setDisc(disc), Integer::valueOf);
			getValueFromMatcher(taggedFile, matcher, "Date", (tag, date) -> tag.setDate(LocalDate.of(date, 1, 1)), Integer::valueOf);
		}
	}

	private Pattern createPattern(String pattern) {
		String regexPattern = pattern
				.replaceAll(Pattern.quote("${Track}"), "(?<Track>\\\\d+)")
				.replaceAll(Pattern.quote("${Artist}"), "(?<Artist>.+)")
				.replaceAll(Pattern.quote("${AlbumArtist}"), "(?<AlbumArtist>.+)")
				.replaceAll(Pattern.quote("${Name}"), "(?<Name>.+)")
				.replaceAll(Pattern.quote("${Album}"), "(?<Album>.+)")
				.replaceAll(Pattern.quote("${Disc}"), "(?<Disc>\\\\d+)")
				.replaceAll(Pattern.quote("${Date}"), "(?<Date>\\\\d+)");
		return Pattern.compile(regexPattern);
	}

	private <T> void getValueFromMatcher(TaggedFile taggedFile, Matcher matcher, String name, BiConsumer<Tag, T> setter, Function<String, T> converter) {
		try {
			String value = matcher.group(name);
			if (value == null) {
				return;
			}
			setter.accept(taggedFile.getTag(), converter.apply(value));
		} catch (IllegalArgumentException iae1) {
			/* ignore. */
		}
	}

	private static class Options {

		@Parameter(names = { "-d", "--directory" })
		private String baseDirectory = "";

		@Parameter
		private List<String> patternValues;

	}

}
