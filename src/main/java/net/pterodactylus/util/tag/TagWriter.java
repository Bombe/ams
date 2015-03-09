package net.pterodactylus.util.tag;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for format-specific tag writers.
 *
 * @author <a href="mailto:">David Roden</a>
 */
public interface TagWriter {

	/**
	 * Checks whether this tag writer can encode all the tag’s fields and their
	 * values. An ID3v1 tag writer, for example, can only encode texts up to 30
	 * characters, and genre is not a free-text field.
	 *
	 * @return {@code true} if the values of the given tag can be encoded, {@code
	 *         false} otherwise
	 */
	boolean isEncodable(Tag tag);

	boolean write(Tag tag, Path file) throws IOException;

}
