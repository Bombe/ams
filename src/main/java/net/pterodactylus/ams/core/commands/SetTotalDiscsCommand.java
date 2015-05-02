package net.pterodactylus.ams.core.commands;

import java.util.List;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies the {@link Tag#getTotalDiscs() total disc} information of the {@link Session}’s files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetTotalDiscsCommand extends SetAttributeCommand {

	@Override
	public String getName() {
		return "totaldiscs";
	}

	@Override
	protected void setAttributes(Context context, List<TaggedFile> selectedFiles, String value) {
		selectedFiles.stream().map(TaggedFile::getTag).forEach(tag -> tag.setTotalDiscs(Integer.valueOf(value)));
	}

}
