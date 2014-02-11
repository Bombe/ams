package net.pterodactylus.util.tag;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Abstract implementation of a {@link Tag} which stores all fields.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractTag implements Tag {

	private Optional<String> name = empty();
	private Optional<String> artist = empty();
	private Optional<String> albumArtist = empty();
	private Optional<String> album = empty();
	private Optional<Integer> track = empty();
	private Optional<Integer> totalTracks = empty();
	private Optional<Integer> disc = empty();
	private Optional<Integer> totalDiscs = empty();
	private Optional<String> genre = empty();
	private Optional<LocalDate> date = empty();
	private Optional<String> comment = empty();

	public void setName(String name) {
		this.name = ofNullable(name);
	}

	public Optional<String> getName() {
		return name;
	}

	public void setArtist(String artist) {
		this.artist = ofNullable(artist);
	}

	public Optional<String> getArtist() {
		return artist;
	}

	public Optional<String> getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = ofNullable(albumArtist);
	}

	public Optional<String> getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = ofNullable(album);
	}

	public Optional<Integer> getTrack() {
		return track;
	}

	public void setTrack(int track) {
		this.track = (track == 0) ? empty() : of(track);
	}

	public Optional<Integer> getTotalTracks() {
		return totalTracks;
	}

	public void setTotalTracks(int totalTracks) {
		this.totalTracks = (totalTracks == 0) ? empty() : of(totalTracks);
	}

	public Optional<Integer> getDisc() {
		return disc;
	}

	public void setDisc(int Disc) {
		this.disc = (Disc == 0) ? empty() : of(Disc);
	}

	public Optional<Integer> getTotalDiscs() {
		return totalDiscs;
	}

	public void setTotalDiscs(int totalDiscs) {
		this.totalDiscs = (totalDiscs == 0) ? empty() : of(totalDiscs);
	}

	public Optional<String> getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = ofNullable(genre);
	}

	public Optional<LocalDate> getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = ofNullable(date);
	}

	public Optional<String> getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = ofNullable(comment);
	}

}
