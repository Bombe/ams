package net.pterodactylus.util.tag;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Reads a {@link Tag} from a {@link File}.
 * <p/>
 * This interface needs to be implemented for every file format that should be
 * parsed.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface TagReader {

	/**
	 * Tries to read one or more tags from the given file.
	 *
	 * @param file
	 * 		The file to read the tags from
	 * @return A list of zero or more tags read from the file
	 * @throws IOException
	 * 		if an I/O error occurs
	 */
	List<Tag> readTags(File file) throws IOException;

}
