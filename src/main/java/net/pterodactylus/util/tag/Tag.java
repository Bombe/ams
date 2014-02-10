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

}
