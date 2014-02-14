package net.pterodactylus.util.tag.id3.v2_3;

import static net.pterodactylus.util.tag.id3.v2_3.Counters.parseCounters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import org.junit.Test;

/**
 * Unit test for {@link Counters}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CountersTest {

	@Test
	public void canParseValidCurrent() {
		Optional<Counters> counters = parseCounters("4");
		assertThat(counters.get().getCurrent(), is(4));
	}

	@Test
	public void canNotParseInvalidCurrent() {
		Optional<Counters> counters = parseCounters("d");
		assertThat(counters.isPresent(), is(false));
	}

	@Test
	public void canParseValidCurrentAndTotal() {
		Optional<Counters> counters = parseCounters("4/13");
		assertThat(counters.get().getCurrent(), is(4));
		assertThat(counters.get().getTotal(), is(13));
	}

	@Test
	public void canParseValidCurrentAndInvalidTotal() {
		Optional<Counters> counters = parseCounters("4/");
		assertThat(counters.get().getCurrent(), is(4));
		assertThat(counters.get().getTotal(), is(0));
	}

	@Test
	public void canParseInvalidCurrentAndValidTotal() {
		Optional<Counters> counters = parseCounters("/13");
		assertThat(counters.get().getCurrent(), is(0));
		assertThat(counters.get().getTotal(), is(13));
	}

}
