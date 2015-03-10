package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.List;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagWriter;
import net.pterodactylus.util.tag.TaggedFile;
import net.pterodactylus.util.tag.id3.v2_3.ID3v23TagWriter;

import com.google.common.annotations.VisibleForTesting;

/**
 * Saves the tags to the files in the {@link Session}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SaveCommand implements Command {

	private final TagWriter tagWriter;

	public SaveCommand() {
		this(new ID3v23TagWriter());
	}

	@VisibleForTesting
	SaveCommand(TagWriter tagWriter) {
		this.tagWriter = tagWriter;
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public void execute(Context context, List<String> parameters) throws IOException {
		for (TaggedFile taggedFile : context.getSession().getFiles()) {
			Tag tag = taggedFile.getTag();
			tagWriter.write(tag, taggedFile.getFile());
		}
	}

}
