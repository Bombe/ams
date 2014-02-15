package net.pterodactylus.util.tag;

import static java.util.Optional.empty;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import net.pterodactylus.util.tag.id3.v1.ID3v1TagReader;
import net.pterodactylus.util.tag.id3.v2_3.ID3v23TagReader;

/**
 * Methods that handle {@link TagReader} instances.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TagReaders {

	public static TagReader defaultTagReaders() {
		return combine(new ID3v23TagReader(), new ID3v1TagReader());
	}

	public static TagReader combine(TagReader... tagReaders) {
		return new TagReader() {
			@Override
			public Optional<Tag> readTags(File file) throws IOException {
				for (TagReader tagReader : tagReaders) {
					Optional<Tag> tag = tagReader.readTags(file);
					if (tag.isPresent()) {
						return tag;
					}
				}
				return empty();
			}
		};
	}

}
