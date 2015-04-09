package se.fearless.common.io;

import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiStreamLocator implements StreamLocator {
	private static final InputHandler INPUT_HANDLER = new InputHandler();
	private static final OutputHandler OUTPUT_HANDLER = new OutputHandler();

	private final StreamLocator[] delegates;

	public MultiStreamLocator(StreamLocator... delegates) {
		this.delegates = delegates;
	}

	@Override
	public InputSupplier<? extends InputStream> getInputSupplier(final String key) {
		return new InputSupplier<InputStream>() {
			@Override
			public InputStream getInput() throws IOException {
				return handle(key, INPUT_HANDLER);
			}
		};
	}

	@Override
	public OutputSupplier<? extends OutputStream> getOutputSupplier(final String key) {
		return new OutputSupplier<OutputStream>() {
			@Override
			public OutputStream getOutput() throws IOException {
				return handle(key, OUTPUT_HANDLER);
			}
		};
	}


	private <T> T handle(String key, Handler<T> handler) throws IOException {
		IOException lastIOException = null;
		RuntimeException lastRuntimeException = null;
		for (StreamLocator delegate : delegates) {
			try {
				T obj = handler.handle(key, delegate);
				if (obj != null) {
					return obj;
				}
			} catch (RuntimeException e) {
				lastRuntimeException = e;
				lastIOException = null;
			} catch (IOException e) {
				lastIOException = e;
				lastRuntimeException = null;
			}
		}
		if (lastRuntimeException != null) {
			throw lastRuntimeException;
		}
		if (lastIOException != null) {
			throw lastIOException;
		}
		throw new RuntimeException("Resource not found: " + key);
	}

	@Override
	public Iterator<String> listKeys() {
		List<String> keys = new ArrayList<String>();
		for (StreamLocator delegate : delegates) {
			Iterator<String> iter = delegate.listKeys();
			while (iter.hasNext()) {
				keys.add(iter.next());
			}
		}
		return keys.iterator();
	}

	private static interface Handler<T> {
		T handle(String key, StreamLocator locator) throws IOException;
	}

	private static class InputHandler implements Handler<InputStream> {
		@Override
		public InputStream handle(String key, StreamLocator locator) throws IOException {
			InputSupplier<? extends InputStream> supplier = locator.getInputSupplier(key);
			return supplier.getInput();
		}
	}

	private static class OutputHandler implements Handler<OutputStream> {
		@Override
		public OutputStream handle(String key, StreamLocator locator) throws IOException {
			OutputSupplier<? extends OutputStream> outputSupplier = locator.getOutputSupplier(key);
			return outputSupplier.getOutput();
		}
	}


}
