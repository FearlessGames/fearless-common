package se.fearless.common.io;

import java.io.OutputStream;
import java.util.function.Supplier;

public interface OutputStreamSupplierLocator {
	Supplier<OutputStream> getOutputStreamSupplier(String key);
}
