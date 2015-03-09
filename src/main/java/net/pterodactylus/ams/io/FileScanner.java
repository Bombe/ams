package net.pterodactylus.ams.io;

import static java.nio.file.Files.walkFileTree;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Scans a directory and notifies a {@link FileProcessor} for every located
 * file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileScanner {

	private final Path root;

	public FileScanner(Path root) {
		this.root = root;
	}

	public void scan(FileProcessor fileProcessor) throws IOException {
		walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
				fileProcessor.processFile(file);
				return super.visitFile(file, attributes);
			}
		});
	}

}
