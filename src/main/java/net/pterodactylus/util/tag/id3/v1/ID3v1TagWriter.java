package net.pterodactylus.util.tag.id3.v1;

import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_ALBUM_LENGTH;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_ARTIST_LENGTH;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_COMMENT_LENGTH;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_NAME_LENGTH;
import static net.pterodactylus.util.tag.id3.v1.Genre.getNumber;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_TRACK_NUMBER;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.REDUCED_MAX_COMMENT_LENGTH;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import net.pterodactylus.util.tag.Tag;
import net.pterodactylus.util.tag.TagWriter;

/**
 * Writes ID3v1 tags to the appropriate places in a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ID3v1TagWriter implements TagWriter {

	private static final ID3v1TagReader tagReader = new ID3v1TagReader();
	private static final ID3v1TagEncoder tagEncoder = new ID3v1TagEncoder();

	@Override
	public boolean isEncodable(Tag tag) {
		if (tag.getName().isPresent() && (tag.getName().get().length() > MAX_NAME_LENGTH)) {
			return false;
		}
		if (tag.getArtist().isPresent() && (tag.getArtist().get().length() > MAX_ARTIST_LENGTH)) {
			return false;
		}
		if (tag.getAlbumArtist().isPresent()) {
			return false;
		}
		if (tag.getAlbum().isPresent() && (tag.getAlbum().get().length() > MAX_ALBUM_LENGTH)) {
			return false;
		}
		if (tag.getTrack().isPresent() && ((tag.getTrack().get() < 0) || (tag.getTrack().get() > MAX_TRACK_NUMBER))) {
			return false;
		}
		if (tag.getTotalTracks().isPresent()) {
			return false;
		}
		if (tag.getDisc().isPresent()) {
			return false;
		}
		if (tag.getTotalDiscs().isPresent()) {
			return false;
		}
		if (tag.getGenre().isPresent() && !getNumber(tag.getGenre().get()).isPresent()) {
			return false;
		}
		if (tag.getComment().isPresent()) {
			if (tag.getComment().get().length() > MAX_COMMENT_LENGTH) {
				return false;
			}
			if (tag.getTrack().isPresent() && (tag.getComment().get().length() > REDUCED_MAX_COMMENT_LENGTH)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean write(Tag tag, File file) throws IOException {
		if (tagReader.readTags(file).isPresent()) {
			overwriteOldTag(file, tag);
		}
		appendTag(file, tag);
		return false;
	}

	private void overwriteOldTag(File file, Tag tag) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
			randomAccessFile.seek(randomAccessFile.length() - 128);
			writeTagToFile(randomAccessFile, tag);
		}
	}

	private void appendTag(File file, Tag tag) throws IOException {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
			randomAccessFile.seek(randomAccessFile.length());
			writeTagToFile(randomAccessFile, tag);
		}
	}

	private void writeTagToFile(RandomAccessFile randomAccessFile, Tag tag) throws IOException {
		randomAccessFile.write(tagEncoder.encode(tag));
	}

}
