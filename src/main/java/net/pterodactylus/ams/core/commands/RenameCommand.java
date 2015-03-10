package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagFormatter;
import net.pterodactylus.util.tag.TaggedFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.annotations.VisibleForTesting;

/**
 * Renames files according to their tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RenameCommand implements Command {

	private final FileSystem fileSystem;

	public RenameCommand() {
		this(FileSystems.getDefault());
	}

	@VisibleForTesting
	RenameCommand(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	@Override
	public String getName() {
		return "rename";
	}

	@Override
	public void execute(Context context, List<String> parameters) throws IOException {
		Options options = new Options();
		new JCommander(options, parameters.toArray(new String[parameters.size()]));
		String pattern = options.patterns.stream().collect(Collectors.joining(" "));
		TagFormatter tagFormatter = new TagFormatter(pattern);
		Session session = context.getSession();
		for (TaggedFile taggedFile : new ArrayList<>(context.getSession().getFiles())) {
			Tag tag = taggedFile.getTag();
			String formattedName = tagFormatter.format(tag);
			Path renamedFile = fileSystem.getPath(options.targetDirectory, formattedName);
			if (options.dryRun) {
				context.write(String.format("%s → %s", taggedFile.getFile(), formattedName));
			} else {
				Path parentDirectory = renamedFile.getParent();
				Files.createDirectories(parentDirectory);
				Files.move(taggedFile.getFile(), renamedFile);
				session.removeFile(taggedFile);
				session.addFile(new TaggedFile(renamedFile, tag));
			}
		}
		context.flush();
	}

	private static class Options {

		@Parameter(names = { "-n", "--dry-run" }, description = "dry run: don’t rename files")
		private boolean dryRun;

		@Parameter(names = { "-d", "--directory" })
		private String targetDirectory = ".";

		@Parameter(description = "pattern for formatting the filenames")
		private List<String> patterns;

	}

}
