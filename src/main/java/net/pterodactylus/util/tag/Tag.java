package net.pterodactylus.util.tag;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Generlization of data that can be stored in tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Tag {

	Optional<String> getName();
	Optional<String> getArtist();
	Optional<String> getAlbumArtist();
	Optional<String> getAlbum();
	Optional<Integer> getTrack();
	Optional<Integer> getTotalTracks();
	Optional<Integer> getDisc();
	Optional<Integer> getTotalDiscs();
	Optional<String> getGenre();
	Optional<LocalDate> getDate();
	Optional<String> getComment();

	/**
	 * Checks whether this tag can encode all its fields and their values. An ID3v1
	 * tag, for example, can only encode texts up to 30 characters, and genre is
	 * not a free-text field.
	 *
	 * @return {@code true} if the values of this tag can be encoded, {@code false}
	 *         otherwise
	 */
	boolean isEncodable();

}
