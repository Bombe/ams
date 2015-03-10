package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.ams.io.FileProcessor;
import net.pterodactylus.ams.io.FileScanner;
import net.pterodactylus.util.media.MediaFileIdentifier;
import net.pterodactylus.util.media.MediaFileIdentifiers;
import net.pterodactylus.util.tag.TaggedFile;

import com.google.common.annotations.VisibleForTesting;

/**
 * {@link Command} implementation that loads all {@link MediaFileIdentifier identified media files} into the session.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 * @see Session#addFile(TaggedFile)
 */
public class LoadCommand implements Command {

	private final MediaFileIdentifier mediaFileIdentifier;
	private final FileSystem fileSystem;

	public LoadCommand() {
		this(FileSystems.getDefault(), MediaFileIdentifiers.defaultMediaFileIdentifiers());
	}

	@VisibleForTesting
	LoadCommand(FileSystem fileSystem, MediaFileIdentifier mediaFileIdentifier) {
		this.fileSystem = fileSystem;
		this.mediaFileIdentifier = mediaFileIdentifier;
	}

	@Override
	public String getName() {
		return "load";
	}

	@Override
	public void execute(Context context, List<String> parameters) throws IOException {
		for (String parameter : parameters) {
			Path file = fileSystem.getPath(parameter);
			if (Files.isDirectory(file)) {
				new FileScanner(file).scan(f -> addFile(context, f));
			} else if (Files.isRegularFile(file)) {
				addFile(context, file);
			} else {
				if (filenameContainsAsterisk(parameter)) {
					addWithGlob(context, parameter);
				}
			}
		}
	}

	private boolean filenameContainsAsterisk(String parameter) {
		return parameter.indexOf('*') != -1;
	}

	private void addWithGlob(Context context, String parameter) throws IOException {
		int lastSlashBeforeAsterisk = parameter.lastIndexOf('/', parameter.indexOf('*'));
		String rootPath = (lastSlashBeforeAsterisk != -1) ? parameter.substring(0, lastSlashBeforeAsterisk) : ".";
		PathMatcher pathMatcher = fileSystem.getPathMatcher("glob:" + parameter);
		Files.walkFileTree(fileSystem.getPath(rootPath), matchingFileAdder(pathMatcher, f -> addFile(context, f)));
	}

	private SimpleFileVisitor<Path> matchingFileAdder(PathMatcher pathMatcher, FileProcessor fileProcessor) {
		return new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
				if (!pathMatcher.matches(path)) {
					return FileVisitResult.CONTINUE;
				}
				fileProcessor.processFile(path);
				return FileVisitResult.CONTINUE;
			}
		};
	}

	private void addFile(Context context, Path file) throws IOException {
		if (mediaFileIdentifier.isMediaFile(file)) {
			context.write(String.format("Adding %s...\n", file));
			context.getSession().addFile(new TaggedFile(file));
		}
	}

}
