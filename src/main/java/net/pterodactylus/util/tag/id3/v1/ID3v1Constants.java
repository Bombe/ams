package net.pterodactylus.util.tag.id3.v1;

/**
 * A collection of commonly used constants when handling ID3v1.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
interface ID3v1Constants {

	int TITLE_OFFSET = 3;
	int ARTIST_OFFSET = 33;
	int ALBUM_OFFSET = 63;
	int YEAR_OFFSET = 93;
	int COMMENT_OFFSET = 97;
	int TRACK_OFFSET = 126;
	int GENRE_OFFSET = 127;

	int MAX_NAME_LENGTH = 30;
	int MAX_ARTIST_LENGTH = 30;
	int MAX_ALBUM_LENGTH = 30;
	int MAX_TRACK_NUMBER = 99;
	int MAX_COMMENT_LENGTH = 30;
	int REDUCED_MAX_COMMENT_LENGTH = 28;

}
