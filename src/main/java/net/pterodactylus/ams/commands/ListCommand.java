package net.pterodactylus.ams.commands;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Session;

/**
 * {@link Command} that lists all {@link Session#getFiles() files} of a
 * session.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ListCommand implements Command {

	@Override
	public void process(Session session, List<String> parameters) throws IOException {
		List<Pattern> patterns = parameters.stream().map(Pattern::compile).collect(toList());
		for (File file : session.getFiles()) {
			if (patterns.isEmpty() || patterns.stream().allMatch((pattern) -> pattern.matcher(file.getPath()).find())) {
				session.getOutput().write(format("%s\n", file.getPath()));
			}
		}
	}

}
