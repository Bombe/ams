package net.pterodactylus.ams.core.commands;

import java.util.List;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies the artist of files in the session.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetArtistCommand extends SetAttributeCommand {

	@Override
	public String getName() {
		return "artist";
	}

	@Override
	protected void setAttributes(Context context, List<TaggedFile> selectedFiles, String value) {
		selectedFiles.stream().map(TaggedFile::getTag).forEach(tag -> tag.setArtist(value));
	}

}
