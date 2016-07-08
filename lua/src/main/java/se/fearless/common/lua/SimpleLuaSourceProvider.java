package se.fearless.common.lua;

import com.google.common.io.ByteSource;
import com.google.inject.Inject;
import se.fearless.common.io.IOLocator;
import se.krka.kahlua.require.LuaSourceProvider;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class SimpleLuaSourceProvider implements LuaSourceProvider {
	private final IOLocator IOLocator;

	@Inject
	public SimpleLuaSourceProvider(final IOLocator IOLocator) {
		this.IOLocator = IOLocator;
	}

	@Override
	public Reader getLuaSource(final String s) {
		ByteSource byteSource = IOLocator.getByteSource(s + ".lua");
		try {
			return byteSource.asCharSource(StandardCharsets.UTF_8).openStream();
		} catch (IOException e) {
			return null;
		}
	}
}
