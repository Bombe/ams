package net.pterodactylus.ams.conversion;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * {@link Source} implementation that calls a FLAC binary to decode a FLAC
 * stream.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FlacSource extends ExternalFilter {

	private final String binary;

	public FlacSource(String binary) {
		this.binary = binary;
	}

	@Override
	protected List<String> getParameters() {
		return asList(
				binary,
				"--decode",
				"-"
		);
	}

	@Override
	public String toString() {
		return "FLAC Source";
	}

}
