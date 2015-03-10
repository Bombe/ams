package net.pterodactylus.util.tag;

import java.util.Optional;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Unit test for {@link TaggedFile}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TaggedFileTest {

	public static Matcher<TaggedFile> isTaggedFile(String filename, Tag tag) {
		return isTaggedFile(filename, Optional.of(tag));
	}

	public static Matcher<TaggedFile> isTaggedFile(String filename, Optional<Tag> tag) {
		return new TypeSafeDiagnosingMatcher<TaggedFile>() {
			@Override
			protected boolean matchesSafely(TaggedFile taggedFile, Description mismatchDescription) {
				if (!taggedFile.getFile().toAbsolutePath().normalize().toString().equals(filename)) {
					mismatchDescription.appendText("file is at ").appendValue(taggedFile.getFile().toString());
					return false;
				}
				if (tag.isPresent() && !taggedFile.getTag().equals(tag.get())) {
					mismatchDescription.appendText("tag is ").appendValue(taggedFile.getTag());
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("file at ").appendValue(filename);
				if (tag.isPresent()) {
					description.appendText(" with tag ").appendValue(tag);
				}
			}
		};
	}

	public static Matcher<TaggedFile> isTaggedFile(String filename) {
		return isTaggedFile(filename, Optional.empty());
	}

}
