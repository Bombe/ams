package net.pterodactylus.ams.conversion;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.google.common.io.ByteStreams;

/**
 * A pipeline can copy data from a {@link Source} over an arbitrary number of
 * {@link Filter}s to a {@link Sink}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Pipeline {

	private static final Logger logger = Logger.getLogger(Pipeline.class.getName());
	private final Source source;
	private final List<Filter> filters = new ArrayList<>();
	private final Sink sink;

	private Pipeline(Source source, List<Filter> filters, Sink sink) {
		this.source = source;
		this.filters.addAll(filters);
		this.sink = sink;
	}

	public static Builder from(Source source) {
		return new Builder(source);
	}

	public void run(ExecutorService executorService) throws IOException {
		List<Copier> copiers = createCopiers();
		List<Future<?>> futures = createFutures(executorService, copiers);
		try {
			waitForCompletion(futures);
		} catch (ExecutionException ee1) {
			if (ee1.getCause() instanceof IOException) {
				throw (IOException) ee1.getCause();
			}
		}
	}

	private List<Copier> createCopiers() {
		Source lastSource = source;
		List<Copier> copiers = new ArrayList<>();
		for (Filter filter : filters) {
			copiers.add(new Copier(lastSource, filter));
			lastSource = filter;
		}
		copiers.add(new Copier(lastSource, sink));
		return copiers;
	}

	private List<Future<?>> createFutures(ExecutorService executorService, List<Copier> copiers) {
		List<Future<?>> futures = new ArrayList<>();
		for (Copier copier : copiers) {
			futures.add(executorService.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					copier.copy();
					return null;
				}
			}));
		}
		return futures;
	}

	private void waitForCompletion(List<Future<?>> futures) throws ExecutionException {
		ExecutionException executionException = null;
		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException ee1) {
				if (executionException == null) {
					executionException = ee1;
				} else {
					executionException.addSuppressed(ee1.getCause());
				}
			}
		}
		if (executionException != null) {
			throw executionException;
		}
	}

	public static class Builder {

		private final Source source;
		private final List<Filter> filters = new ArrayList<>();

		private Builder(Source source) {
			this.source = source;
		}

		public Builder through(Filter filter) {
			filters.add(filter);
			return this;
		}

		public Pipeline to(Sink sink) {
			return new Pipeline(source, filters, sink);
		}

	}

	private static class Copier {

		private static final Logger logger = Logger.getLogger(Copier.class.getName());
		private final Source source;
		private final Sink sink;

		private Copier(Source source, Sink sink) {
			this.source = source;
			this.sink = sink;
		}

		public void copy() throws IOException {
			logger.info(() -> format("Copying from %s to %s...", source, sink));
			try (InputStream sourceOutput = source.getInputStream(); OutputStream sinkInput = sink.getOutputStream()) {
				ByteStreams.copy(sourceOutput, sinkInput);
				sinkInput.flush();
			}
		}

	}

}
