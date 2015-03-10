package net.pterodactylus.ams.core.commands;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies the {@link Tag#getTrack() track} information of the {@link Session}’s files
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetTrackCommand extends SetAttributeCommand {

	@Override
	public String getName() {
		return "track";
	}

	@Override
	protected void setAttributes(List<TaggedFile> selectedFiles, String value) {
		AtomicInteger index = new AtomicInteger();
		selectedFiles.stream().map(TaggedFile::getTag).forEach(tag -> {
			index.incrementAndGet();
			if ("auto".equalsIgnoreCase(value)) {
				tag.setTrack(index.get());
				tag.setTotalTracks(selectedFiles.size());
			} else {
				tag.setTrack(Integer.valueOf(value));
			}
		});
	}

}
