package net.pterodactylus.ams.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import com.google.common.jimfs.Jimfs;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link PathSource}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PathSourceTest {

	private final FileSystem fileSystem = Jimfs.newFileSystem();
	private final Path sourceFile = fileSystem.getPath("/file");
	private final PathSource source = new PathSource(sourceFile);
	private final byte[] randomData;

	public PathSourceTest() {
		Random random = new Random();
		randomData = new byte[4096];
		random.nextBytes(randomData);
	}

	@Before
	public void setupSourceFile() throws IOException {
		Files.write(sourceFile, randomData, StandardOpenOption.CREATE);
	}

	@Test
	public void inputStreamReturnsCorrectData() throws IOException {
		try (InputStream sourceInputStream = source.getInputStream()) {
			MatcherAssert.assertThat(sourceInputStream, delivers(randomData));
		}
	}

	@Test
	public void toStringContainsSourcePrefix() {
		MatcherAssert.assertThat(source.toString(), Matchers.startsWith("source:"));
	}

	@Test
	public void toStringContainsFilePrefix() {
		MatcherAssert.assertThat(source.toString(), Matchers.containsString(":file://"));
	}

	@Test
	public void toStringContainsFilename() {
		MatcherAssert.assertThat(source.toString(), Matchers.containsString(sourceFile.toString()));
	}

	public static Matcher<InputStream> delivers(final byte[] data) {
		return new TypeSafeDiagnosingMatcher<InputStream>() {
			byte[] readData = new byte[data.length];

			@Override
			protected boolean matchesSafely(InputStream inputStream, Description mismatchDescription) {
				int offset = 0;
				try {
					while (true) {
						int r = inputStream.read();
						if (r == -1) {
							return offset == data.length;
						}
						if (offset == data.length) {
							return false;
						}
						if (data[offset] != (readData[offset] = (byte) r)) {
							mismatchDescription.appendText("byte at ")
									.appendValue(offset)
									.appendText(" is ")
									.appendValue(r);
							return false;
						}
						offset++;
					}
				} catch (IOException ioe1) {
					return false;
				}
			}

			@Override
			public void describeTo(Description description) {
				description.appendValue(data);
			}
		};
	}

}
