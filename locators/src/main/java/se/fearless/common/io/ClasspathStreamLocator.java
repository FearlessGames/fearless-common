package se.fearless.common.io;

import com.google.common.collect.Iterators;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.function.Supplier;

public class ClasspathStreamLocator implements StreamLocator {

	private final Class<?> clazz;

	public ClasspathStreamLocator() {
		this(ClasspathStreamLocator.class);
	}

	public ClasspathStreamLocator(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Supplier<? extends InputStream> getInputStreamSupplier(final String key) {
		return new Supplier<InputStream>() {
			@Override
			public InputStream get() {
				String path = key;
				if (path.contains("/") && !path.startsWith("/")) {
					path = "/" + path;
				}

				InputStream resource = clazz.getResourceAsStream(path);
				if (resource == null) {
					throw new RuntimeException("Could not find resource " + path);
				}
				return resource;
			}
		};
	}

	@Override
	public Supplier<? extends OutputStream> getOutputStreamSupplier(final String key) {
		return new Supplier<OutputStream>() {
			@Override
			public OutputStream get() {
				throw new RuntimeException("Cannot write to a classpath resource");
			}
		};
	}

	@Override
	public Iterator<String> listKeys() {
		return Iterators.emptyIterator();
	}

}
