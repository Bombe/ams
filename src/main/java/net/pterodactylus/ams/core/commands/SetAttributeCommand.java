package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.TaggedFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Modifies an attribute of all files or a range of files in the session.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class SetAttributeCommand implements Command {

	@Override
	public void execute(Context context, List<String> parameters) throws IOException {
		Options options = new Options();
		new JCommander(options, parameters.toArray(new String[parameters.size()]));
		String attributeValue = options.values.stream().collect(Collectors.joining(" "));
		int fileIndex = 0;
		Predicate<Integer> inRange = CommandUtils.isInRange(options.tracks, context.getSession().getFiles().size());
		List<TaggedFile> selectedFiles = new ArrayList<>();
		for (TaggedFile taggedFile : context.getSession().getFiles()) {
			fileIndex++;
			if (inRange.test(fileIndex)) {
				selectedFiles.add(taggedFile);
			}
		}
		setAttributes(selectedFiles, attributeValue);
	}

	protected abstract void setAttributes(List<TaggedFile> files, String value);

	private static class Options {

		@Parameter(names = { "-t", "--tracks" })
		private String tracks;

		@Parameter
		private List<String> values;

	}

}
