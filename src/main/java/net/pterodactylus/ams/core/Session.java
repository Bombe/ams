package net.pterodactylus.ams.core;

import static java.util.Collections.unmodifiableCollection;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A session binds together the execution of several {@link Command}s and can
 * be used to transport data from one command to another.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Session {

	private final Set<File> files = new HashSet<>();

	public void addFile(File file) {
		files.add(file);
	}

	public Collection<File> getFiles() {
		return unmodifiableCollection(files);
	}

}
