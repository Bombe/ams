package net.pterodactylus.ams.conversion;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * Runs the input stream through a LAME encoder and outputs an MP3 stream.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LameEncoder extends ExternalFilter {

	private final String binary;

	public LameEncoder(String binary) {
		this.binary = binary;
	}

	@Override
	protected List<String> getParameters() {
		return asList(
				binary,
				"--preset", "standard",
				"-q", "0",
				"-p",
				"-T",
				"-",
				"-"
		);
	}

	@Override
	public String toString() {
		return "LAME Sink (--preset standard)";
	}

}
