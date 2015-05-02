package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies the {@link Tag#getName() name} of the {@link Session}’s files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetNameCommand extends AbstractCommand {

	@Override
	public String getName() {
		return "name";
	}

	@Override
	protected void executeForFiles(Context context, List<TaggedFile> selectedFiles, List<String> parameters)
	throws IOException {
		Supplier<String> nameSupplier = new Supplier<String>() {
			private boolean returnedFirst = parameters.isEmpty();
			private String firstParameter = parameters.stream().collect(Collectors.joining(" "));
			@Override
			public String get() {
				if (!returnedFirst) {
					returnedFirst = true;
					return firstParameter;
				}
				try {
					return context.getNextLine();
				} catch (IOException ioe1) {
					return null;
				}
			}
		};
		for (TaggedFile selectedFile : selectedFiles) {
			String name = nameSupplier.get();
			if (name == null) {
				break;
			}
			selectedFile.getTag().setName(name);
		}
	}

}
