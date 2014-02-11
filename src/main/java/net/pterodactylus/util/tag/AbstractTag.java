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

	public Optional<String> getName() {
		return name;
	}

	public Tag setName(String name) {
		this.name = ofNullable(name);
		return this;
	}

	public Optional<String> getArtist() {
		return artist;
	}

	public Tag setArtist(String artist) {
		this.artist = ofNullable(artist);
		return this;
	}

	public Optional<String> getAlbumArtist() {
		return albumArtist;
	}

	public Tag setAlbumArtist(String albumArtist) {
		this.albumArtist = ofNullable(albumArtist);
		return this;
	}

	public Optional<String> getAlbum() {
		return album;
	}

	public Tag setAlbum(String album) {
		this.album = ofNullable(album);
		return this;
	}

	public Optional<Integer> getTrack() {
		return track;
	}

	public Tag setTrack(int track) {
		this.track = (track == 0) ? empty() : of(track);
		return this;
	}

	public Optional<Integer> getTotalTracks() {
		return totalTracks;
	}

	public Tag setTotalTracks(int totalTracks) {
		this.totalTracks = (totalTracks == 0) ? empty() : of(totalTracks);
		return this;
	}

	public Optional<Integer> getDisc() {
		return disc;
	}

	public Tag setDisc(int Disc) {
		this.disc = (Disc == 0) ? empty() : of(Disc);
		return this;
	}

	public Optional<Integer> getTotalDiscs() {
		return totalDiscs;
	}

	public Tag setTotalDiscs(int totalDiscs) {
		this.totalDiscs = (totalDiscs == 0) ? empty() : of(totalDiscs);
		return this;
	}

	public Optional<String> getGenre() {
		return genre;
	}

	public Tag setGenre(String genre) {
		this.genre = ofNullable(genre);
		return this;
	}

	public Optional<LocalDate> getDate() {
		return date;
	}

	public Tag setDate(LocalDate date) {
		this.date = ofNullable(date);
		return this;
	}

	public Optional<String> getComment() {
		return comment;
	}

	public Tag setComment(String comment) {
		this.comment = ofNullable(comment);
		return this;
	}

}
