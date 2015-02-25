package net.pterodactylus.util.tag.id3.v2_3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Performs unsychronization necessary in ID3v2 tags.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Unsynchronizer {

	public boolean isUnsychronizationRequired(byte[] data) {
		if (data.length < 2) {
			return false;
		}
		for (int offset = 0; offset < data.length - 1; offset++) {
			if (((data[offset] & 0xff) == 0xff) && ((data[offset + 1] & 0xe0) == 0xe0)) {
				return true;
			}
		}
		return false;
	}

	public byte[] unsynchronize(byte[] data) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length * 4 / 3)) {
			boolean previousWas0xFF = false;
			for (int dataByte : data) {
				if (previousWas0xFF && ((dataByte & 0xe0) == 0xe0)) {
					outputStream.write(0);
					outputStream.write(dataByte);
					previousWas0xFF = false;
					continue;
				}
				if ((dataByte & 0xff) == 0xff) {
					previousWas0xFF = true;
				}
				outputStream.write(dataByte);
			}
			return outputStream.toByteArray();
		} catch (IOException ioe1) {
			/* ignore, won’t happen. */
			throw new IllegalStateException("ByteArrayOutputStream threw exception", ioe1);
		}
	}

}
