package net.pterodactylus.ams.core.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.List;

import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * Modifies {@link Tag#getDate() date} information of the {@link Session}’s files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetDateCommand extends SetAttributeCommand {

	private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
			.appendValue(ChronoField.YEAR, 4)
			.optionalStart()
			.appendLiteral('-')
			.appendValue(ChronoField.MONTH_OF_YEAR, 2)
			.appendLiteral('-')
			.appendValue(ChronoField.DAY_OF_MONTH, 2)
			.optionalStart()
			.appendLiteral("-01-01")
			.optionalEnd()
			.optionalEnd()
			.toFormatter();

	@Override
	public String getName() {
		return "date";
	}

	@Override
	protected void setAttributes(Context context, List<TaggedFile> selectedFiles, String value) {
		LocalDate date;
		try {
			date = LocalDate.parse(value + "-01-01", DATE_FORMAT);
		} catch (DateTimeParseException dtpe1) {
			return;
		}
		selectedFiles.stream().map(TaggedFile::getTag).forEach(tag -> tag.setDate(date));
	}

}
