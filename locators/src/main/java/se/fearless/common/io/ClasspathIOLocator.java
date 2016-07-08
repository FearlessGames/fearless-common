package se.fearless.common.io;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class ClasspathIOLocator implements IOLocator {

	private final Class<?> clazz;

	public ClasspathIOLocator() {
		this(ClasspathIOLocator.class);
	}

	public ClasspathIOLocator(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public ByteSource getByteSource(final String key) {
		return new ByteSource() {
			@Override
			public InputStream openStream() throws IOException {

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
	public ByteSink getByteSink(final String key) {
		return new ByteSink() {
			@Override
			public OutputStream openStream() throws IOException {
				throw new IOException("Cannot write to a classpath resource");
			}
		};
	}

	@Override
	public Iterator<String> listKeys() {
		return ImmutableSet.<String>of().iterator();
	}

}
