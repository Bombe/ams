package net.pterodactylus.ams.conversion;

import static com.google.common.io.Closer.create;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import net.pterodactylus.util.io.InputStreamDrainer;

import com.google.common.io.Closer;

/**
 * Runs an executable as {@link Filter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class ExternalFilter implements Closeable, AutoCloseable, Filter {

	private final Closer closer = create();
	private Process process;
	private InputStream inputStream;
	private OutputStream outputStream;
	private InputStream errorStream;

	@Override
	public InputStream getInputStream() throws IOException {
		openProcess();
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		openProcess();
		return outputStream;
	}

	private synchronized void openProcess() throws IOException {
		if (process != null) {
			return;
		}
		process = new ProcessBuilder(getParameters()).start();
		new Thread(new InputStreamDrainer(errorStream = process.getErrorStream())).start();
		inputStream = process.getInputStream();
		outputStream = process.getOutputStream();
		closer.register(inputStream);
		closer.register(outputStream);
		closer.register(errorStream);
	}

	protected abstract List<String> getParameters();

	@Override
	public void close() throws IOException {
		closer.close();
	}

}
