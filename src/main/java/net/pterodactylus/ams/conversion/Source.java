package net.pterodactylus.ams.conversion;

import java.io.IOException;
import java.io.InputStream;

/**
 * Supplies an {@link InputStream} from an arbitrary source.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Source {

	InputStream getInputStream() throws IOException;

}
