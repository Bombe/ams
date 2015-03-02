package net.pterodactylus.util.media;

import java.io.File;
import java.io.IOException;

/**
 * Identifies if a {@link File} is a media file of a certain format, depending on the concrete implementation.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface MediaFileIdentifier {

	boolean isMediaFile(File file) throws IOException;

}
