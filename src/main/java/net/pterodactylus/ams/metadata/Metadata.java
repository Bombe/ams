package net.pterodactylus.ams.metadata;

/**
 * Metadata container.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Metadata {

	public enum FileType {

		AUDIO,

	}

	private final FileType type;

	public Metadata(FileType type) {
		this.type = type;
	}

	public FileType getType() {
		return type;
	}

}
