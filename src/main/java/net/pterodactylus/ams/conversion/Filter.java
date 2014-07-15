package net.pterodactylus.ams.conversion;

/**
 * Combines a {@link Source} and a {@link Sink} to create a filter which
 * can convert one kind of data into another kind of data.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Filter extends Source, Sink {

}
