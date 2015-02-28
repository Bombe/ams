package net.pterodactylus.ams.core;

import java.io.IOException;
import java.util.List;

/**
 * A command can be used to manipulate a {@link Session}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Command {

	String getName();
	void execute(Context context, List<String> parameters) throws IOException;

}
