package net.pterodactylus.ams.conversion;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Supplies an {@link OutputStream} to an arbitrary sink.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Sink {

	OutputStream getOutputStream() throws IOException;

}
