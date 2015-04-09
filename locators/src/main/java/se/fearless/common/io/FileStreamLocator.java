package se.fearless.common.io;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class FileStreamLocator implements StreamLocator {
	private final File root;

	public FileStreamLocator(final File root) {
		this.root = root;
	}

	@Override
	public InputSupplier<? extends InputStream> getInputSupplier(final String key) {
		return Files.newInputStreamSupplier(new File(root, key));
	}

	@Override
	public OutputSupplier<? extends OutputStream> getOutputSupplier(final String key) {
		return Files.newOutputStreamSupplier(new File(root, key));
	}

	@Override
	public Iterator<String> listKeys() {
		return new SimpleFileSystemIterator().iterator(root);
	}
}
