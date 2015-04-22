package net.pterodactylus.ams.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Parses a line of text into words.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LineParser {

	public Optional<Result> parse(String line) throws EmptyLine {
		if (line == null) {
			throw new EmptyLine();
		}
		List<String> words = splitLineIntoWords(line);
		return words.isEmpty() ? Optional.empty() :
				Optional.of(new Result(words.get(0), words.subList(1, words.size())));
	}

	private List<String> splitLineIntoWords(String line) {
		List<String> words = new ArrayList<>();
		StringBuilder currentWord = new StringBuilder();
		boolean inSingleQuotes = false;
		boolean inDoubleQuotes = false;
		boolean inBackslash = false;
		for (char c : line.toCharArray()) {
			if (inSingleQuotes) {
				if (c == '\'') {
					inSingleQuotes = false;
				} else {
					currentWord.append(c);
				}
			} else if (inBackslash) {
				currentWord.append(c);
				inBackslash = false;
			} else if (inDoubleQuotes) {
				if (c == '\\') {
					inBackslash = true;
				} else if (c == '"') {
					inDoubleQuotes = false;
				} else {
					currentWord.append(c);
				}
			} else if (Character.isWhitespace(c)) {
				if (currentWord.length() > 0) {
					words.add(currentWord.toString());
					currentWord.setLength(0);
				}
			} else if (c == '\\') {
				inBackslash = true;
			} else if (c == '\'') {
				inSingleQuotes = true;
			} else if (c == '"') {
				inDoubleQuotes = true;
			} else {
				currentWord.append(c);
			}
		}
		if (currentWord.length() > 0) {
			words.add(currentWord.toString());
		}
		return words;
	}

	/**
	 * The result of {@link LineParser#parse(String)}, consisting of a command name and a list of parameters.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Result {

		private final String command;
		private final List<String> parameters;

		public Result(String command, List<String> parameters) {
			this.command = command;
			this.parameters = parameters;
		}

		public String getCommand() {
			return command;
		}

		public List<String> getParameters() {
			return parameters;
		}

	}

	/**
	 * Signals that a {@code null} line was given into {@link LineParser#parse(String)}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class EmptyLine extends RuntimeException { }

}
