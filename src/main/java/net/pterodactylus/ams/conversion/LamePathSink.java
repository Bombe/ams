package net.pterodactylus.ams.conversion;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * A {@code LameFileEncoder} is a combination of a {@link LameEncoder} and a {@link FileSink}. It has the advantage over
 * the {@link LameEncoder} that it can write a LAME tag to the encoded file which enables skipping in variable-bitrate
 * files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LamePathSink implements Sink {

	private final LameEncoder lameEncoder;
	private final Path destinationFile;

	public LamePathSink(String binary, Path destinationFile) {
		this.lameEncoder = new LameEncoder(binary) {
			@Override
			protected List<String> getParameters() {
				List<String> parameters = super.getParameters();
				parameters.set(parameters.size() - 1, destinationFile.toAbsolutePath().toString());
				return parameters;
			}
		};
		this.destinationFile = destinationFile;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return lameEncoder.getOutputStream();
	}

	@Override
	public String toString() {
		return "sink:lame:file//" + destinationFile.toAbsolutePath().toString();
	}

}
