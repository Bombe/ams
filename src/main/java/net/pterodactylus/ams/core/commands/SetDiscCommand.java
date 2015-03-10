package net.pterodactylus.ams.core.commands;

import java.util.List;

import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies the {@link Tag#getDisc() disc} information of the {@link Session}’s files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetDiscCommand extends SetAttributeCommand {

	@Override
	public String getName() {
		return "disc";
	}

	@Override
	protected void setAttributes(List<TaggedFile> selectedFiles, String value) {
		if ("auto".equalsIgnoreCase(value)) {
			int discs = 0;
			int lastTrack = Integer.MAX_VALUE;
			for (Tag tag : selectedFiles.stream().map(TaggedFile::getTag).toArray(l -> new Tag[l])) {
				if (tag.getTrack().orElse(0) < lastTrack) {
					discs++;
				}
				lastTrack = tag.getTrack().orElse(0);
			}
			lastTrack = Integer.MAX_VALUE;
			int disc = 0;
			for (Tag tag : selectedFiles.stream().map(TaggedFile::getTag).toArray(l -> new Tag[l])) {
				if (tag.getTrack().orElse(0) < lastTrack) {
					disc++;
				}
				lastTrack = tag.getTrack().orElse(0);
				tag.setDisc(disc);
				tag.setTotalDiscs(discs);
			}
			return;
		}
		selectedFiles.stream().map(TaggedFile::getTag).forEach(tag -> tag.setDisc(Integer.valueOf(value)));
	}

}
