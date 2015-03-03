package net.pterodactylus.util.tag;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Formats tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TagFormatter {

	private final String pattern;

	public TagFormatter(String pattern) {
		this.pattern = pattern;
	}

	public String format(Tag tag) {
		return parsePattern(pattern, tag);
	}

	private String parsePattern(String pattern, Tag tag) {
		int inDollar = 0;
		boolean lastWasDollar = false;
		StringBuilder output = new StringBuilder();
		StringBuilder expression = new StringBuilder();
		String attribute = null;
		String format = null;
		boolean inFormat = false;
		for (char c : pattern.toCharArray()) {
			if (lastWasDollar) {
				if (c == '$') {
					expression.append(c);
				} else {
					if (c != '{') {
						throw new ExpectedLeftCurlyBrace();
					}
					inDollar++;
					output.append(expression);
					expression.setLength(0);
				}
				lastWasDollar = false;
			} else if (c == '$') {
				lastWasDollar = true;
			} else if (inDollar > 0) {
				if (c == '}') {
					String defaultValue;
					if (attribute == null) {
						attribute = expression.toString();
						defaultValue = "";
					} else {
						if (inFormat) {
							format = expression.toString();
							defaultValue = "";
						} else {
							defaultValue = expression.toString();
						}
					}
					Optional<?> value = getTagAttribute(tag, attribute);
					if (!value.isPresent()) {
						output.append(defaultValue);
					} else {
						if (format == null) {
							format = (value.get() instanceof LocalDate) ? "tY" : "s";
						}
						output.append(String.format("%" + format, value.get()));
					}
					attribute = null;
					expression.setLength(0);
					inDollar--;
				} else if (c == '%') {
					attribute = expression.toString();
					inFormat = true;
					expression.setLength(0);
				} else if (c == ',') {
					if (attribute == null) {
						attribute = expression.toString();
					} else {
						format = expression.toString();
					}
					inFormat = false;
					expression.setLength(0);
				} else {
					expression.append(c);
				}
			} else {
				expression.append(c);
			}
		}
		if (expression.length() > 0) {
			output.append(expression);
		}
		if (inDollar > 0) {
			throw new ExpectedRightCurlyBrace();
		}
		return output.toString();
	}

	private Optional<?> getTagAttribute(Tag tag, String attribute) {
		if (attribute.equals("Name")) {
			return tag.getName();
		}
		if (attribute.equals("Artist")) {
			return tag.getArtist();
		}
		if (attribute.equals("AlbumArtist")) {
			return tag.getAlbumArtist();
		}
		if (attribute.equals("Album")) {
			return tag.getAlbum();
		}
		if (attribute.equals("Comment")) {
			return tag.getComment();
		}
		if (attribute.equals("Track")) {
			return tag.getTrack();
		}
		if (attribute.equals("TotalTracks")) {
			return tag.getTotalTracks();
		}
		if (attribute.equals("Disc")) {
			return tag.getDisc();
		}
		if (attribute.equals("TotalDiscs")) {
			return tag.getTotalDiscs();
		}
		if (attribute.equals("Date")) {
			return tag.getDate();
		}
		throw new InvalidTagAttribute();
	}

	public static class ExpectedLeftCurlyBrace extends RuntimeException { }

	public static class ExpectedRightCurlyBrace extends RuntimeException { }

	public static class InvalidTagAttribute extends RuntimeException { }

}
