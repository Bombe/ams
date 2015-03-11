package net.pterodactylus.ams.core.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.TaggedFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Parses the track selection from a list of parameters.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SelectedFiles {

	private final List<TaggedFile> selectedFiles;
	private final List<String> parameters;

	public SelectedFiles(List<TaggedFile> selectedFiles, List<String> parameters) {
		this.selectedFiles = Collections.unmodifiableList(selectedFiles);
		this.parameters = Collections.unmodifiableList(parameters);
	}

	public List<TaggedFile> getSelectedFiles() {
		return selectedFiles;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public static SelectedFiles createFrom(Context context, List<String> parameters) {
		Options options = new Options();
		JCommander jCommander = parseParameters(parameters, options);
		List<TaggedFile> selectedFiles = getSelectedFiles(context, options.tracks);
		return new SelectedFiles(selectedFiles, collectRemainingParameters(options, jCommander));
	}

	private static JCommander parseParameters(List<String> parameters, Options options) {
		JCommander jCommander = new JCommander(options);
		jCommander.setAcceptUnknownOptions(true);
		jCommander.parse(parameters.toArray(new String[parameters.size()]));
		return jCommander;
	}

	private static List<String> collectRemainingParameters(Options options, JCommander jCommander) {
		List<String> parameters = new ArrayList<>(options.parameters);
		parameters.addAll(jCommander.getUnknownOptions());
		return parameters;
	}

	private static List<TaggedFile> getSelectedFiles(Context context, String trackSpecification) {
		int fileIndex = 0;
		Predicate<Integer> inRange = CommandUtils.isInRange(trackSpecification, context.getSession().getFiles().size());
		List<TaggedFile> selectedFiles = new ArrayList<>();
		for (TaggedFile taggedFile : context.getSession().getFiles()) {
			fileIndex++;
			if (inRange.test(fileIndex)) {
				selectedFiles.add(taggedFile);
			}
		}
		return selectedFiles;
	}

	private static class Options {

		@Parameter(names = { "-t", "--tracks" })
		public String tracks;

		@Parameter
		public List<String> parameters = new ArrayList<>();

	}

}
