package se.fearlessgames.common.io;

import com.google.common.collect.Iterators;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class ClasspathStreamLocator implements StreamLocator {

	private final Class<?> clazz;

	public ClasspathStreamLocator() {
		this(ClasspathStreamLocator.class);
	}

	public ClasspathStreamLocator(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public InputSupplier<? extends InputStream> getInputSupplier(final String key) {
		return new InputSupplier<InputStream>() {
			@Override
			public InputStream getInput() throws IOException {
				String path = key;
				if (path.contains("/") && !path.startsWith("/")) {
					path = "/" + path;
				}

				InputStream resource = clazz.getResourceAsStream(path);
				if (resource == null) {
					throw new IOException("Could not find resource " + path);
				}
				return resource;
			}
		};
	}

	@Override
	public OutputSupplier<? extends OutputStream> getOutputSupplier(final String key) {
		return new OutputSupplier<OutputStream>() {
			@Override
			public OutputStream getOutput() throws IOException {
				throw new IOException("Cannot write to a classpath resource");
			}
		};
	}

	@Override
	public Iterator<String> listKeys() {
		return Iterators.emptyIterator();
	}

}
