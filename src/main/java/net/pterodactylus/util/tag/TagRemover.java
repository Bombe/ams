package net.pterodactylus.util.tag;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A tag remover can remove a single tag from a file.
 * <p/>
 * This interface needs to be implemented for every tag format.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface TagRemover {

	/**
	 * Removes a tag from the given file.
	 *
	 * @param file
	 * 		The file to remove a tag from
	 * @return {@code true} if a tag was removed, {@code false} otherwise
	 * @throws IOException
	 * 		if an I/O error occurs
	 */
	boolean removeTag(Path file) throws IOException;

}
