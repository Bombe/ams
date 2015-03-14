package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Modifies {@link Tag#getName() name} and {@link Tag#getArtist() artist} information for several files at once, with
 * different values.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BatchCommand extends AbstractCommand {

	@Override
	public String getName() {
		return "batch";
	}

	@Override
	protected void executeForFiles(Context context, List<TaggedFile> selectedFiles, List<String> parameters)
	throws IOException {
		Options options = new Options();
		new JCommander(options, parameters.toArray(new String[parameters.size()]));
		LinkedList<String> remainingValues = new LinkedList<>(options.values);
		for (TaggedFile selectedFile : selectedFiles) {
			if (remainingValues.isEmpty()) {
				break;
			}
			String value = remainingValues.removeFirst();
			if (options.writeNames) {
				selectedFile.getTag().setName(value);
			}
			if (options.writeArtists) {
				selectedFile.getTag().setArtist(value);
			}
		}
	}

	private static class Options {

		@Parameter(names = { "--name" })
		private boolean writeNames;

		@Parameter(names = { "--artist" })
		private boolean writeArtists;

		@Parameter
		private List<String> values;

	}

}
