package se.fearless.common.io;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public class OutputStreamWriterSupplier {

	public static Supplier<OutputStreamWriter> asOutputStreamWriter(OutputStream outputStream, Charset charset) {
		return () -> new OutputStreamWriter(outputStream, charset);
	}
}
