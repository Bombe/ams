package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies the {@link Tag#getName() name} of the {@link Session}’s files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetNameCommand extends AbstractCommand {

	@Override
	public String getName() {
		return "name";
	}

	@Override
	protected void executeForFiles(Context context, List<TaggedFile> selectedFiles, List<String> parameters)
	throws IOException {
		boolean returnedFirst = parameters.isEmpty();
		int index = returnedFirst ? 0 : 1;
		for (TaggedFile selectedFile : selectedFiles) {
			if (!returnedFirst) {
				selectedFile.getTag().setName(parameters.stream().collect(Collectors.joining(" ")));
				returnedFirst = true;
			} else {
				String name = context.getNextLine(String.format("%d/%d: ", ++index, selectedFiles.size()));
				if (name == null) {
					break;
				}
				selectedFile.getTag().setName(name);
			}
		}
	}

}
