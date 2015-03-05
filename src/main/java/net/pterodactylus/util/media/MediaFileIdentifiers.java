package net.pterodactylus.util.media;

import java.io.IOException;

/**
 * Provides access to a default set of {@link MediaFileIdentifier}s, as well as helper methods for working with {@link
 * MediaFileIdentifier}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MediaFileIdentifiers {

	public static MediaFileIdentifier defaultMediaFileIdentifiers() {
		return combine(new Mp3Identifier(), new FlacIdentifier(), new RiffWaveIdentifier());
	}

	public static MediaFileIdentifier combine(MediaFileIdentifier... mediaFileIdentifiers) {
		return file -> {
			IOException ioException = null;
			for (MediaFileIdentifier mediaFileIdentifier : mediaFileIdentifiers) {
				try {
					if (mediaFileIdentifier.isMediaFile(file)) {
						return true;
					}
				} catch (IOException ioe1) {
					if (ioException == null) {
						ioException = new IOException();
					}
					ioException.addSuppressed(ioe1);
				}
			}
			if (ioException != null) {
				throw ioException;
			}
			return false;
		};
	}

}
