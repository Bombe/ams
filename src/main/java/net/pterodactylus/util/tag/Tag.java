package net.pterodactylus.util.tag;

import java.io.File;
import java.io.IOException;
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

	Tag setName(String name);
	Tag setArtist(String artist);
	Tag setAlbumArtist(String albumArtist);
	Tag setAlbum(String album);
	Tag setTrack(int track);
	Tag setTotalTracks(int totalTracks);
	Tag setDisc(int disc);
	Tag setTotalDiscs(int totalDiscs);
	Tag setGenre(String genre);
	Tag setDate(LocalDate date);
	Tag setComment(String comment);

}
