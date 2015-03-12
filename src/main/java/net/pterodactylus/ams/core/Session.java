package net.pterodactylus.ams.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.pterodactylus.util.tag.TaggedFile;

/**
 * A session is a unit of files that is processed together.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Session {

	private final List<TaggedFile> taggedFiles = new ArrayList<>();

	public void addFile(TaggedFile file) {
		taggedFiles.add(file);
	}

	public void removeFile(TaggedFile file) {
		taggedFiles.remove(file);
	}

	public void replaceFile(TaggedFile oldFile, TaggedFile newFile) {
		int oldIndex = taggedFiles.indexOf(oldFile);
		if (oldIndex == -1) {
			return;
		}
		taggedFiles.set(oldIndex, newFile);
	}

	public List<TaggedFile> getFiles() {
		return Collections.unmodifiableList(taggedFiles);
	}

}
