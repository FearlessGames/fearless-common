package se.fearless.common.io;

import com.google.common.io.ByteSource;

import java.util.Iterator;

public interface ByteSourceLocator {
	ByteSource getByteSource(String key);

	Iterator<String> listKeys();
}
