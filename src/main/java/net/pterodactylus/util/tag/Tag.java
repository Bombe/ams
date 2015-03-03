package net.pterodactylus.util.tag;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Generlization of data that can be stored in tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Tag {

	private static final Pattern WHITESPACE_STRIPPER =Pattern.compile("^[ \t\r\n\u00a0]*(.*?)[ \t\r\n\u00a0]*$");

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

	public Tag() {
	}

	public Tag(Tag tag) {
		this.name = tag.name;
		this.artist = tag.artist;
		this.albumArtist = tag.albumArtist;
		this.album = tag.album;
		this.track = tag.track;
		this.totalTracks = tag.totalTracks;
		this.disc = tag.disc;
		this.totalDiscs = tag.totalDiscs;
		this.genre = tag.genre;
		this.date = tag.date;
		this.comment = tag.comment;
	}

	public Optional<String> getName() {
		return name;
	}

	public Tag setName(String name) {
		this.name = stripWhitespace(name);
		return this;
	}

	private Optional<String> stripWhitespace(String name) {
		if (name == null) {
			return Optional.empty();
		}
		String strippedName = WHITESPACE_STRIPPER.matcher(name).replaceAll("$1");
		return strippedName.isEmpty() ? Optional.empty() : Optional.of(strippedName);
	}

	public Optional<String> getArtist() {
		return artist;
	}

	public Tag setArtist(String artist) {
		this.artist = stripWhitespace(artist);
		return this;
	}

	public Optional<String> getAlbumArtist() {
		return albumArtist;
	}

	public Tag setAlbumArtist(String albumArtist) {
		this.albumArtist = stripWhitespace(albumArtist);
		return this;
	}

	public Optional<String> getAlbum() {
		return album;
	}

	public Tag setAlbum(String album) {
		this.album = stripWhitespace(album);
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
		this.comment = stripWhitespace(comment);
		return this;
	}

}
