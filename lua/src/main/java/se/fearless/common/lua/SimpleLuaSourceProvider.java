package se.fearless.common.lua;

import com.google.inject.Inject;
import se.fearless.common.io.InputReaderSupplier;
import se.fearless.common.io.StreamLocator;
import se.krka.kahlua.require.LuaSourceProvider;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class SimpleLuaSourceProvider implements LuaSourceProvider {
	private final StreamLocator streamLocator;

	@Inject
	public SimpleLuaSourceProvider(final StreamLocator streamLocator) {
		this.streamLocator = streamLocator;
	}

	@Override
	public Reader getLuaSource(final String s) {
		Supplier<InputStream> inputStreamSupplier = streamLocator.getInputStreamSupplier("/" + s + ".lua");
		return InputReaderSupplier.asInputReaderSupplier(inputStreamSupplier, StandardCharsets.UTF_8).get();
	}
}
