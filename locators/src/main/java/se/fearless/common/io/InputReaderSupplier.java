package se.fearless.common.io;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public class InputReaderSupplier {

	public static Supplier<InputStreamReader> asInputReaderSupplier(InputStream inputStream, Charset charset) {
		return () -> {
			return new InputStreamReader(inputStream, charset);
		};
	}
}
