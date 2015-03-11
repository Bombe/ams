package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.List;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Abstract {@link Command} implementation that handles track selection parameters.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractCommand implements Command {

	@Override
	public final void execute(Context context, List<String> parameters) throws IOException {
		SelectedFiles selectedFiles = SelectedFiles.createFrom(context, parameters);
		executeForFiles(context, selectedFiles.getSelectedFiles(), selectedFiles.getParameters());
	}

	protected abstract void executeForFiles(Context context, List<TaggedFile> selectedFiles, List<String> parameters)
	throws IOException;

}
