package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.List;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Context;

/**
 * {@link Command} that tells the {@link Context} that it should {@link Context#exit()}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class QuitCommand implements Command {

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public void execute(Context context, List<String> parameters) throws IOException {
		context.exit();
	}

}
