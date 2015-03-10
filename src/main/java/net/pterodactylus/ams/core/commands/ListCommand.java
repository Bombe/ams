package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Shows information about all files in the session.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ListCommand implements Command {

	private static final String BOLD_ON = "\u001b[1m";
	private static final String BOLD_OFF = "\u001b[0m";

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public void execute(Context context, List<String> parameters) throws IOException {
		int fileIndex = 1;
		for (TaggedFile taggedFile : context.getSession().getFiles()) {
			context.write(String.format("[%d] %s:\n", fileIndex++, taggedFile.getFile()));
			showTag(context, taggedFile.getTag());
		}
	}

	private void showTag(Context context, Tag tag) throws IOException {
		dumpInfo(context, "Artist", tag.getArtist());
		dumpInfo(context, "Name", tag.getName());
		dumpInfo(context, "Album", tag.getAlbum());
		dumpInfo(context, "Album Artist", tag.getAlbumArtist());
		dumpInfo(context, "Date", tag.getDate());
		if (tag.getTrack().isPresent()) {
			context.write(String.format("  Track: %s%s%s", BOLD_ON, tag.getTrack().get(), BOLD_OFF));
			if (tag.getTotalTracks().isPresent()) {
				context.write(String.format(" / %s%s%s", BOLD_ON, tag.getTotalTracks().get(), BOLD_OFF));
			}
			context.write("\n");
		}
		if (tag.getDisc().isPresent()) {
			context.write(String.format("  Disc: %s%s%s", BOLD_ON, tag.getDisc().get(), BOLD_OFF));
			if (tag.getTotalDiscs().isPresent()) {
				context.write(String.format(" / %s%s%s", BOLD_ON, tag.getTotalDiscs().get(), BOLD_OFF));
			}
			context.write("\n");
		}
		dumpInfo(context, "Genre", tag.getGenre());
		dumpInfo(context, "Comment", tag.getComment());
	}

	private void dumpInfo(Context context, String title, Optional<?> attribute) throws IOException {
		if (!attribute.isPresent()) {
			return;
		}
		context.write("  ");
		context.write(title + ": ");
		context.write(BOLD_ON);
		context.write(attribute.get().toString());
		context.write(BOLD_OFF);
		context.write("\n");
	}

}
