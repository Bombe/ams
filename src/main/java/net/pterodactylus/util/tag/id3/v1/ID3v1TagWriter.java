package net.pterodactylus.util.tag.id3.v1;

import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_ALBUM_LENGTH;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_ARTIST_LENGTH;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_COMMENT_LENGTH;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_NAME_LENGTH;
import static net.pterodactylus.util.tag.id3.v1.Genre.getNumber;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.MAX_TRACK_NUMBER;
import static net.pterodactylus.util.tag.id3.v1.ID3v1Constants.REDUCED_MAX_COMMENT_LENGTH;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

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
	public void write(Tag tag, Path file) throws IOException {
		if (tagReader.readTags(file).isPresent()) {
			overwriteOldTag(file, tag);
		}
		appendTag(file, tag);
	}

	private void overwriteOldTag(Path file, Tag tag) throws IOException {
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(file, StandardOpenOption.WRITE)) {
			seekableByteChannel.position(seekableByteChannel.size() - 128);
			writeTagToFile(seekableByteChannel, tag);
		}
	}

	private void appendTag(Path file, Tag tag) throws IOException {
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(file, StandardOpenOption.APPEND)) {
			writeTagToFile(seekableByteChannel, tag);
		}
	}

	private void writeTagToFile(SeekableByteChannel seekableByteChannel, Tag tag) throws IOException {
		seekableByteChannel.write(ByteBuffer.wrap(tagEncoder.encode(tag)));
	}

}
