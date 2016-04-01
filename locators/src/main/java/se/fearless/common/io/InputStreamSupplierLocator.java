package se.fearless.common.io;

import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Supplier;

public interface InputStreamSupplierLocator {
	Supplier<? extends InputStream> getInputStreamSupplier(String key);

	Iterator<String> listKeys();
}
