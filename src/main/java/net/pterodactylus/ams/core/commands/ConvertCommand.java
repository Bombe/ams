package net.pterodactylus.ams.core.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.pterodactylus.ams.conversion.FlacSource;
import net.pterodactylus.ams.conversion.LamePathSink;
import net.pterodactylus.ams.conversion.PathSource;
import net.pterodactylus.ams.conversion.Pipeline;
import net.pterodactylus.ams.core.Command;
import net.pterodactylus.ams.core.Context;
import net.pterodactylus.ams.core.Session;
import net.pterodactylus.util.tag.TaggedFile;

/**
 * {@link Command} implementation that convert files from one format to another.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ConvertCommand implements Command {

	private static final ExecutorService executorService = Executors.newCachedThreadPool(target -> {
		Thread thread = new Thread(target);
		thread.setDaemon(true);
		return thread;
	});
	private final Converter converter;

	public ConvertCommand() {
		this((context, source, destination) -> {
			Pipeline.Builder pipelineBuilder = Pipeline.from(new PathSource(source));
			if (source.toString().endsWith("flac")) {
				pipelineBuilder.through(new FlacSource(context.getOptions().flacBinary));
			}
			Pipeline pipeline = pipelineBuilder.to(new LamePathSink(context.getOptions().lameBinary, destination));
			pipeline.run(executorService);
		});
	}

	public ConvertCommand(Converter converter) {
		this.converter = converter;
	}

	@Override
	public String getName() {
		return "convert";
	}

	@Override
	public void execute(Context context, List<String> parameters) throws IOException {
		Session session = context.getSession();
		for (TaggedFile taggedFile : new ArrayList<>(context.getSession().getFiles())) {
			Path sourceFile = taggedFile.getFile();
			Path destinationFile = sourceFile.resolveSibling(sourceFile.getFileName().toString() + ".mp3");
			converter.convert(context, sourceFile, destinationFile);
			session.addFile(new TaggedFile(destinationFile, taggedFile.getTag()));
			session.removeFile(taggedFile);
		}
	}

	static interface Converter {

		void convert(Context context, Path sourceFile, Path destinationFile) throws IOException;

	}

}
