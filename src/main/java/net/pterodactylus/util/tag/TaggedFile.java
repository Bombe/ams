package net.pterodactylus.util.tag;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Wrapper around a {@link File} that can also retrieve the {@link Tag} from the file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TaggedFile implements Supplier<Optional<Tag>> {

	private final TagReader defaultTagReaders = TagReaders.defaultTagReaders();
	private final File file;

	public TaggedFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	@Override
	public Optional<Tag> get() {
		try {
			return defaultTagReaders.readTags(file);
		} catch (IOException ioe1) {
			return Optional.empty();
		}
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TaggedFile)) {
			return false;
		}
		TaggedFile taggedFile = (TaggedFile) object;
		return file.equals(taggedFile.file);
	}

}
