package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.util.tag.TaggedFile;

import com.beust.jcommander.Parameter;

/**
 * Modifies an attribute of all files or a range of files in the session.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class SetAttributeCommand extends AbstractCommand {

	@Override
	protected void executeForFiles(Context context, List<TaggedFile> selectedFiles, List<String> parameters)
	throws IOException {
		String attributeValue = parameters.stream().collect(Collectors.joining(" "));
		setAttributes(context, selectedFiles, attributeValue);
	}

	protected abstract void setAttributes(Context context, List<TaggedFile> files, String value);

}
