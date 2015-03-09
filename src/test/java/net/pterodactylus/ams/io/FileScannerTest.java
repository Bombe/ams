package net.pterodactylus.ams.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * Unit test for {@link FileScanner}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileScannerTest {

	@Test
	public void scanDirectory() throws IOException {
		FileScanner fileScanner = new FileScanner(Paths.get("src/test/resources"));
		Set<Path> files = new HashSet<>();
		fileScanner.scan(files::add);
		assertThat(files, containsInAnyOrder(
				Paths.get("src/test/resources/files/test.unknown"),
				Paths.get("src/test/resources/files/second.id3v1.mp3"),
				Paths.get("src/test/resources/files/a/second.id3v2.mp3"),
				Paths.get("src/test/resources/files/a/b/second.vorbis.flac"),
				Paths.get("src/test/resources/files/c/second.vorbis.ogg")
		));
	}

	@Test
	public void scanningASingleFileResultsInThatFile() throws IOException {
		FileScanner fileScanner = new FileScanner(Paths.get("src/test/resources/files/second.id3v1.mp3"));
		Set<Path> files = new HashSet<>();
		fileScanner.scan(files::add);
		assertThat(files, contains(
				Paths.get("src/test/resources/files/second.id3v1.mp3")
		));
	}

}
