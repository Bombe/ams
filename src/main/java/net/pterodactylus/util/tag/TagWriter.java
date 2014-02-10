package net.pterodactylus.util.tag;

import java.io.File;
import java.io.IOException;

/**
 * Writes a tag to a file.
 * <p/>
 * Implementations needs to take care to verify that the given file is able to
 * hold a tag of the given format, i.e. an ID3 writer should not modify Ogg
 * Vorbis files. The writer also decides which fields of the tag are written to
 * the file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface TagWriter {

	/**
	 * Tries to write the given tag to the given file. A tag might not be written
	 * if this writer does not support the file format of the file.
	 *
	 * @param file
	 * 		The file to write to
	 * @param tag
	 * 		The tag to write
	 * @return {@code true} if the tag was written to the file, {@code false}
	 *         otherwise
	 * @throws IOException
	 * 		if an I/O error occurs
	 */
	boolean writeTag(File file, Tag tag) throws IOException;

}
