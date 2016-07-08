package se.fearless.common.io;


import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.File;
import java.util.Iterator;

public class FileLocator implements IOLocator {
	private final File root;

	public FileLocator(final File root) {
		this.root = root;
	}

	@Override
	public ByteSource getByteSource(final String key) {
		return Files.asByteSource(new File(root, key));
	}

	@Override
	public ByteSink getByteSink(final String key) {
		File file = new File(root, key);
		return Files.asByteSink(file);
	}

	@Override
	public Iterator<String> listKeys() {
		return new SimpleFileSystemIterator().iterator(root);
	}
}
