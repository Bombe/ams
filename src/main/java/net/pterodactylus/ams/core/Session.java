package net.pterodactylus.ams.core;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * A session is a unit of files that is processed together.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Session {

	private final Map<TaggedFile, Optional<Tag>> fileTags = new HashMap<>();
	private final Tag sessionTag = new Tag();

	public void addFile(TaggedFile file) {
		fileTags.put(file, file.get());
	}

	public Collection<TaggedFile> getFiles() {
		return fileTags.keySet();
	}

	public Optional<String> getName(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getName);
	}

	private <T> Optional<T> getTagValuePreferringSession(Optional<Tag> tag, Function<Tag, Optional<T>> valueExtractor) {
		return valueExtractor.apply(sessionTag).isPresent() ? valueExtractor.apply(sessionTag) :
				(tag.isPresent() ? valueExtractor.apply(tag.get()) : Optional.<T>empty());
	}

	public boolean isSessionName() {
		return sessionTag.getName().isPresent();
	}

	public void setName(String name) {
		sessionTag.setName(name);
	}

	public Optional<String> getAlbum(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getAlbum);
	}

	public boolean isSessionAlbum() {
		return sessionTag.getAlbum().isPresent();
	}

	public void setAlbum(String album) {
		sessionTag.setAlbum(album);
	}

	public Optional<String> getArtist(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getArtist);
	}

	public boolean isSessionArtist() {
		return sessionTag.getArtist().isPresent();
	}

	public void setArtist(String artist) {
		sessionTag.setArtist(artist);
	}

	public Optional<String> getAlbumArtist(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getAlbumArtist);
	}

	public boolean isSessionAlbumArtist() {
		return sessionTag.getAlbumArtist().isPresent();
	}

	public void setAlbumArtist(String albumArtist) {
		sessionTag.setAlbumArtist(albumArtist);
	}

	public Optional<String> getComment(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getComment);
	}

	public boolean isSessionComment() {
		return sessionTag.getComment().isPresent();
	}

	public void setComment(String comment) {
		sessionTag.setComment(comment);
	}

	public Optional<String> getGenre(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getGenre);
	}

	public boolean isSessionGenre() {
		return sessionTag.getGenre().isPresent();
	}

	public void setGenre(String genre) {
		sessionTag.setGenre(genre);
	}

	public Optional<Integer> getTrack(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getTrack);
	}

	public boolean isSessionTrack() {
		return sessionTag.getTrack().isPresent();
	}

	public void setTrack(int track) {
		sessionTag.setTrack(track);
	}

	public Optional<Integer> getTotalTracks(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getTotalTracks);
	}

	public boolean isSessionTotalTracks() {
		return sessionTag.getTotalTracks().isPresent();
	}

	public void setTotalTracks(int totalTracks) {
		sessionTag.setTotalTracks(totalTracks);
	}

	public Optional<Integer> getDisc(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getDisc);
	}

	public boolean isSessionDisc() {
		return sessionTag.getDisc().isPresent();
	}

	public void setDisc(int disc) {
		sessionTag.setDisc(disc);
	}

	public Optional<Integer> getTotalDiscs(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getTotalDiscs);
	}

	public boolean isSessionTotalDiscs() {
		return sessionTag.getTotalDiscs().isPresent();
	}

	public void setTotalDiscs(int totalDiscs) {
		sessionTag.setTotalDiscs(totalDiscs);
	}

	public Optional<LocalDate> getDate(TaggedFile file) {
		return getTagValuePreferringSession(fileTags.get(file), Tag::getDate);
	}

	public boolean isSessionDate() {
		return sessionTag.getDate().isPresent();
	}

	public void setDate(LocalDate date) {
		sessionTag.setDate(date);
	}

}
