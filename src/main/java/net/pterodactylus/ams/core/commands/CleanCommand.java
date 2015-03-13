package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Fixes capitalization of text-based tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CleanCommand extends AbstractCommand {

	private static final List<String> NON_CAPITALIZABLE_WORDS = Arrays.asList(
			"a", "an", "the", "at", "by", "for", "in", "of", "on", "to", "up", "and", "as", "but", "it", "or", "nor"
	);

	@Override
	public String getName() {
		return "clean";
	}

	@Override
	protected void executeForFiles(Context context, List<TaggedFile> selectedFiles, List<String> parameters)
	throws IOException {
		Options options = new Options();
		new JCommander(options, parameters.toArray(new String[parameters.size()]));
		boolean cleaningIsRestricted = options.cleanArtists || options.cleanNames || options.cleanAlbums
				|| options.cleanAlbumArtists || options.cleanComments || options.cleanGenres;
		for (TaggedFile taggedFile : selectedFiles) {
			Tag tag = taggedFile.getTag();
			if (!cleaningIsRestricted || options.cleanArtists) {
				cleanValue(tag, Tag::getArtist, Tag::setArtist);
			}
			if (!cleaningIsRestricted || options.cleanNames) {
				cleanValue(tag, Tag::getName, Tag::setName);
			}
			if (!cleaningIsRestricted || options.cleanAlbums) {
				cleanValue(tag, Tag::getAlbum, Tag::setAlbum);
			}
			if (!cleaningIsRestricted || options.cleanAlbumArtists) {
				cleanValue(tag, Tag::getAlbumArtist, Tag::setAlbumArtist);
			}
			if (!cleaningIsRestricted || options.cleanComments) {
				cleanValue(tag, Tag::getComment, Tag::setComment);
			}
			if (!cleaningIsRestricted || options.cleanGenres) {
				cleanValue(tag, Tag::getGenre, Tag::setGenre);
			}
		}
	}

	private void cleanValue(Tag tag, Function<Tag, Optional<String>> getter, BiConsumer<Tag, String> setter) {
		if (getter.apply(tag).isPresent()) {
			setter.accept(tag, clean(getter.apply(tag).get()));
		}
	}

	private String clean(String string) {
		List<String> words = Arrays.asList(string.split("[\\p{javaWhitespace}]+"));
		int index = 0;
		for (String word : words) {
			if ((index == 0) || (index == (words.size() - 1))) {
				words.set(index, capitalizeWord(word));
			} else {
				words.set(index, smartCapitalizeWord(word));
			}
			index++;
		}
		return words.stream().collect(Collectors.joining(" "));
	}

	private String smartCapitalizeWord(String word) {
		return NON_CAPITALIZABLE_WORDS.contains(word.toLowerCase()) ? word.toLowerCase() :
				capitalizeWord(word);
	}

	private String capitalizeWord(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}

	private static class Options {

		@Parameter(names = { "--artist" })
		private boolean cleanArtists;

		@Parameter(names = { "--name" })
		private boolean cleanNames;

		@Parameter(names = { "--album" })
		private boolean cleanAlbums;

		@Parameter(names = { "--albumartist" })
		private boolean cleanAlbumArtists;

		@Parameter(names = { "--comment" })
		private boolean cleanComments;

		@Parameter(names = { "--genre" })
		private boolean cleanGenres;

	}

}
