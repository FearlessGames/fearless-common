package se.fearless.common.io;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.function.Supplier;

public class FileStreamLocator implements StreamLocator {
	private final File root;

	public FileStreamLocator(final File root) {
		this.root = root;
	}

	@Override
	public Supplier<InputStream> getInputStreamSupplier(final String key) {
		return () -> {
			try {
				return Files.newInputStream(new File(root, key).toPath());
			} catch (IOException e) {
				throw new RuntimeException("Failed to get input stream to " + key, e);
			}
		};

	}

	@Override
	public Supplier<OutputStream> getOutputStreamSupplier(final String key) {
		File file = new File(root, key);
		return () -> {
			try {
				return Files.newOutputStream(file.toPath());
			} catch (IOException e) {
				throw new RuntimeException("Failed to get output stream to " + key, e);
			}
		};
	}

	@Override
	public Iterator<String> listKeys() {
		return new SimpleFileSystemIterator().iterator(root);
	}
}
