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
		for (TaggedFile taggedFile : selectedFiles) {
			Tag tag = taggedFile.getTag();
			cleanValue(tag, Tag::getArtist, Tag::setArtist);
			cleanValue(tag, Tag::getName, Tag::setName);
			cleanValue(tag, Tag::getAlbum, Tag::setAlbum);
			cleanValue(tag, Tag::getAlbumArtist, Tag::setAlbumArtist);
			cleanValue(tag, Tag::getComment, Tag::setComment);
			cleanValue(tag, Tag::getGenre, Tag::setGenre);
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

}
