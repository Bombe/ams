package net.pterodactylus.util.tag;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Reads a {@link Tag} from a {@link File}.
 * <p/>
 * This interface needs to be implemented for every file format that should be
 * parsed.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
@FunctionalInterface
public interface TagReader {

	/**
	 * Tries to read a tag from the given file.
	 *
	 * @param file
	 * 		The file to read the tag from
	 * @return The tag read from the file, or {@link Optional#empty()} if no tag
	 *         could be found
	 * @throws IOException
	 * 		if an I/O error occurs
	 */
	Optional<Tag> readTags(File file) throws IOException;

}
