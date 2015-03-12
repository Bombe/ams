package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.List;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Removes selected files from the session.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RemoveCommand extends AbstractCommand {

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	protected void executeForFiles(Context context, List<TaggedFile> selectedFiles, List<String> parameters)
	throws IOException {
		for (TaggedFile taggedFile : selectedFiles) {
			context.getSession().removeFile(taggedFile);
		}
	}

}
