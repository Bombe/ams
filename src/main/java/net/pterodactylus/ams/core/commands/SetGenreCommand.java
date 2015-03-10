package net.pterodactylus.ams.core.commands;

import java.util.List;

import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies {@link Tag#getGenre() genre} information of the {@link Session}’s files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetGenreCommand extends SetAttributeCommand {

	@Override
	public String getName() {
		return "genre";
	}

	@Override
	protected void setAttributes(List<TaggedFile> selectedFiles, String value) {
		selectedFiles.stream().map(TaggedFile::getTag).forEach(tag -> tag.setGenre(value));
	}

}
