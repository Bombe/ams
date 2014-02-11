package net.pterodactylus.util.tag.id3.v1;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.util.tag.id3.v1.Genre.getName;
import static net.pterodactylus.util.tag.id3.v1.Genre.getNumber;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit test for {@link Genre}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class GenreTest {

	@Test
	public void nameForANegativeNumberDoesNotExist() {
		assertThat(getName(-2), is(empty()));
	}

	@Test
	public void nameForAVeryLargeNumberDoesNotExist() {
		assertThat(getName(800), is(empty()));
	}

	@Test
	public void returnNameForAValidNumber() {
		assertThat(getName(100), is(of("Humour")));
	}

	@Test
	public void numberForAnInvalidNameDoesNotExist() {
		assertThat(getNumber("Hugendubel"), is(empty()));
	}

	@Test
	public void numberForAValidNameExists() {
		assertThat(getNumber("Humour"), is(of(100)));
	}

	@Test
	public void numberForAValidNameWithDifferenceCaseExists() {
		assertThat(getNumber("hUmOuR"), is(of(100)));
	}

}
