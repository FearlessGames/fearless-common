package se.fearless.common.io;

import com.google.common.io.ByteSink;

public interface ByteSinkLocator {
	ByteSink getByteSink(String key);
}
