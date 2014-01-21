package net.pterodactylus.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizes a string into words that may be quoted (using double quotes,
 * single quotes, or backslashes).
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Tokenizer {

	public static List<String> tokenize(String text) {
		List<String> words = new ArrayList<>();
		StringBuilder currentWord = new StringBuilder();
		boolean startedNewWord = false;
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		boolean inBackslash = false;
		for (char c : text.toCharArray()) {
			if (inBackslash) {
				currentWord.append(c);
				inBackslash = false;
			} else if (isBackslash(c)) {
				inBackslash = true;
				startedNewWord = true;
			} else if (isDoubleQuote(c)) {
				if (inDoubleQuote) {
					inDoubleQuote = false;
				} else if (inSingleQuote) {
					currentWord.append(c);
				} else {
					startedNewWord = true;
					inDoubleQuote = true;
				}
			} else if (isSingleQuote(c)) {
				if (inSingleQuote) {
					inSingleQuote = false;
				} else if (inDoubleQuote) {
					currentWord.append(c);
				} else {
					inSingleQuote = true;
					startedNewWord = true;
				}
			} else if (isSpace(c)) {
				if (inDoubleQuote || inSingleQuote) {
					currentWord.append(c);
				} else {
					if (startedNewWord) {
						words.add(currentWord.toString());
						currentWord.setLength(0);
						startedNewWord = false;
					}
				}
			} else {
				startedNewWord = true;
				currentWord.append(c);
			}
		}
		if (startedNewWord) {
			words.add(currentWord.toString());
		}
		return words;
	}

	private static boolean isSpace(char c) {
		return c == ' ';
	}

	private static boolean isSingleQuote(char c) {
		return c == '\'';
	}

	private static boolean isDoubleQuote(char c) {
		return c == '"';
	}

	private static boolean isBackslash(char c) {
		return c == '\\';
	}

}
