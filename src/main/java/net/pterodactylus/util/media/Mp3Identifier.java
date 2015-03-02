package net.pterodactylus.util.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.pterodactylus.util.tag.TagReader;
import net.pterodactylus.util.tag.TagReaders;
import net.pterodactylus.util.tag.id3.v1.ID3v1TagReader;
import net.pterodactylus.util.tag.id3.v2_3.ID3v23TagReader;

import com.google.common.annotations.VisibleForTesting;

/**
 * {@link MediaFileIdentifier} that can identify MP3 files. The detection is somewhat basic; it only checks for a valid
 * MPEG sync sequence (as specified by <a href="http://www.mp3-tech.org/programmer/frame_header.html">mp3-tech.org/…/frame_header.html</a>).
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Mp3Identifier implements MediaFileIdentifier {

	private final TagReader tagReader;

	public Mp3Identifier() {
		this(TagReaders.combine(new ID3v23TagReader(), new ID3v1TagReader()));
	}

	@VisibleForTesting
	Mp3Identifier(TagReader tagReader) {
		this.tagReader = tagReader;
	}

	@Override
	public boolean isMediaFile(File file) throws IOException {
		return tagReader.readTags(file).isPresent()
				|| fileHasMp3SyncBitsInTheFirst4K(file);
	}

	private boolean fileHasMp3SyncBitsInTheFirst4K(File file) throws IOException {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			boolean lastByteWas0xFF = false;
			int bytesRead = 0;
			while (bytesRead < 4096) {
				int data = fileInputStream.read();
				if (data == -1) {
					return false;
				}
				if (lastByteWas0xFF && ((data & 0xe0) == 0xe0)) {
					return true;
				}
				lastByteWas0xFF = (data & 0xff) == 0xff;
				bytesRead++;
			}
		}
		return false;
	}

}
