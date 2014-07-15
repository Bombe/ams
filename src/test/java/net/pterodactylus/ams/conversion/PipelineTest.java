package net.pterodactylus.ams.conversion;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static net.pterodactylus.ams.conversion.Pipeline.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.junit.Test;

/**
 * Unit test for {@link Pipeline}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PipelineTest {

	private final ExecutorService threadPool = newCachedThreadPool();

	@Test
	public void pipelineMovesDataFromSourceThroughFilterToSink() throws IOException {
		Source source = new ByteArraySource("Test".getBytes());
		Filter filter = new DoublingFilter();
		ByteArraySink sink = new ByteArraySink();
		from(source).through(filter).to(sink).run(threadPool);
		assertThat(sink.toByteArray(), is("TTeesstt".getBytes()));
	}

	@Test(expected = IOException.class)
	public void exceptionsInSourceAreCaught() throws IOException {
		Source source = new ThrowingFilter();
		Filter filter = new DoublingFilter();
		Sink sink = new ByteArraySink();
		from(source).through(filter).to(sink).run(threadPool);
	}

	@Test(expected = IOException.class)
	public void exceptionsInSinkAreCaught() throws IOException {
		Source source = new ByteArraySource("Test".getBytes());
		Filter filter = new DoublingFilter();
		Sink sink = new ThrowingFilter();
		from(source).through(filter).to(sink).run(threadPool);
	}

	@Test(expected = IOException.class)
	public void exceptionsInFilterAreCaught() throws IOException {
		Source source = new ByteArraySource("Test".getBytes());
		Filter filter = new ThrowingFilter();
		Sink sink = new ByteArraySink();
		from(source).through(filter).to(sink).run(threadPool);
	}

	private static class ThrowingInputStream extends InputStream {

		@Override
		public int read() throws IOException {
			throw new IOException();
		}

	}

	private static class ThrowingOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			throw new IOException();
		}

	}

	private static class ThrowingFilter implements Filter {

		@Override
		public InputStream getInputStream() throws IOException {
			return new ThrowingInputStream();
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return new ThrowingOutputStream();
		}

	}

	private static class ByteArraySource implements Source {

		private final byte[] data;

		private ByteArraySource(byte[] data) {
			this.data = data;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(data);
		}

	}

	private static class DoublingFilter implements Filter {

		private final List<Byte> queue = new ArrayList<>();
		private boolean closed;

		@Override
		public InputStream getInputStream() throws IOException {
			return new InputStream() {
				@Override
				public int read() throws IOException {
					synchronized (queue) {
						while (true) {
							if (!queue.isEmpty()) {
								return queue.remove(0);
							}
							if (closed) {
								return -1;
							}
							try {
								queue.wait();
							} catch (InterruptedException ie1) {
								throw new RuntimeException(ie1);
							}
						}
					}
				}
			};
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					synchronized (queue) {
						queue.add((byte) b);
						queue.add((byte) b);
						queue.notifyAll();
					}
				}

				@Override
				public void close() throws IOException {
					synchronized (queue) {
						closed = true;
						queue.notifyAll();
					}
				}
			};
		}

	}

	private static class ByteArraySink implements Sink {

		private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		@Override
		public OutputStream getOutputStream() throws IOException {
			return byteArrayOutputStream;
		}

		public byte[] toByteArray() {
			return byteArrayOutputStream.toByteArray();
		}

	}

}
