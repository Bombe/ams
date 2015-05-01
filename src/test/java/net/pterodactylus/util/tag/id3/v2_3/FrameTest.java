package net.pterodactylus.util.tag.id3.v2_3;

import net.pterodactylus.util.tag.id3.v2_3.Frame.NotATextFrame;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Frame}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FrameTest {

	@Test
	public void textFrameWithLatin1TextIsEncodedCorrectly() {
		Frame encodedFrame = Frame.createTextFrame("TIT2", "Nice Latin-1 Title");
		byte[] expectedFrame = new byte[] {
				'T', 'I', 'T', '2', // frame identifier
				0, 0, 0, 19, // size
				0, 0, // flags
				0, // latin-1 identifier
				'N', 'i', 'c', 'e', ' ', 'L', 'a', 't', 'i', 'n', '-', '1', ' ', 'T', 'i', 't', 'l', 'e'
		};
		MatcherAssert.assertThat(encodedFrame.encode(), Matchers.is(expectedFrame));
	}

	@Test
	public void textFrameWithUnicodeTextIsEncodedCorrectly() {
		Frame encodedFrame = Frame.createTextFrame("TIT2", "Uni\u037Cde Title");
		byte[] expectedFrame = new byte[] {
				'T', 'I', 'T', '2', // frame identifier
				0, 0, 0, 27, // size
				0, 0, // flags
				1, // utf16 identifier
				(byte) 0xfe, (byte) 0xff, // UTF Byte Order Mark
				0x00, 'U', 0x00, 'n', 0x00, 'i', 0x03, 0x7c, 0x00, 'd', 0x00, 'e', 0x00, ' ',
				0x00, 'T', 0x00, 'i', 0x00, 't', 0x00, 'l', 0x00, 'e'
		};
		MatcherAssert.assertThat(encodedFrame.encode(), Matchers.is(expectedFrame));
	}

	@Test
	public void nameOfTextFrameCanStartWithAT() {
		Frame.createTextFrame("TIT2", "Some Latin-1 Title");
	}

	@Test
	public void nameOfTextFrameCanNotStartWithAnythingButT() {
		for (char c = 'A'; c <= 'Z'; c++) {
			if (c == 'T') {
				continue;
			}
			try {
				Frame.createTextFrame(c + "ABC", "Value");
				Assert.fail();
			} catch (NotATextFrame natf1) {
			}
		}
	}

	@Test(expected = NotATextFrame.class)
	public void nameOfTextFrameCanNotBeTXXX() {
		Frame.createTextFrame("TXXX", "Value");
	}

}
