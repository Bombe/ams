package net.pterodactylus.ams.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.io.File;
import java.io.IOException;
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
		FileScanner fileScanner = new FileScanner(new File("src/test/resources"));
		Set<File> files = new HashSet<>();
		fileScanner.scan(files::add);
		assertThat(files, containsInAnyOrder(
				new File("src/test/resources/files/second.id3v1.mp3"),
				new File("src/test/resources/files/a/second.id3v2.mp3"),
				new File("src/test/resources/files/a/b/second.vorbis.flac"),
				new File("src/test/resources/files/c/second.vorbis.ogg")
		));
	}


}
