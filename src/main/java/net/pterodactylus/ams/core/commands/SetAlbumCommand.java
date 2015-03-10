package net.pterodactylus.ams.core.commands;

import java.util.List;

import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies the {@link Tag#getAlbum() album} of the {@link Session}’s files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetAlbumCommand extends SetAttributeCommand {

	@Override
	public String getName() {
		return "album";
	}

	@Override
	protected void setAttributes(List<TaggedFile> selectedFiles, String value) {
		selectedFiles.stream().map(TaggedFile::getTag).forEach(tag -> tag.setAlbum(value));
	}

}
