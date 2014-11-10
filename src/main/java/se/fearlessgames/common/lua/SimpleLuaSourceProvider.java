package se.fearlessgames.common.lua;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import se.fearlessgames.common.io.StreamLocator;
import se.krka.kahlua.require.LuaSourceProvider;

import java.io.IOException;
import java.io.Reader;

public class SimpleLuaSourceProvider implements LuaSourceProvider {
	private final StreamLocator streamLocator;

	@Inject
	public SimpleLuaSourceProvider(final StreamLocator streamLocator) {
		this.streamLocator = streamLocator;
	}

	@Override
	public Reader getLuaSource(final String s) {
		try {
			return CharStreams.newReaderSupplier(streamLocator.getInputSupplier("/" + s + ".lua"), Charsets.UTF_8).getInput();
		} catch (IOException e) {
			//throw new RuntimeException(e);
			return null;
		}
	}
}
