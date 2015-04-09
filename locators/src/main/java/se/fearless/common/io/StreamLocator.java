package se.fearless.common.io;

import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public interface StreamLocator {
	InputSupplier<? extends InputStream> getInputSupplier(String key);
	OutputSupplier<? extends OutputStream> getOutputSupplier(String key);
	Iterator<String> listKeys();
}
