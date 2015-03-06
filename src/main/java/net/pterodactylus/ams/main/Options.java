package net.pterodactylus.ams.main;

import net.pterodactylus.util.envopt.Option;

/**
 * Environment-based configuration options.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Options {

	@Option(name = "FLAC_BINARY")
	public final String flacBinary = null;

	@Option(name = "LAME_BINARY")
	public final String lameBinary = null;

}
