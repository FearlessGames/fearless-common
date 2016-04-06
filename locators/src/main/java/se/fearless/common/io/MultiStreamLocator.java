package se.fearless.common.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class MultiStreamLocator implements StreamLocator {
	private static final InputHandler INPUT_HANDLER = new InputHandler();
	private static final OutputHandler OUTPUT_HANDLER = new OutputHandler();

	private final StreamLocator[] delegates;

	public MultiStreamLocator(StreamLocator... delegates) {
		this.delegates = delegates;
	}

	@Override
	public Supplier<InputStream> getInputStreamSupplier(final String key) {
		return () -> handle(key, INPUT_HANDLER);
	}

	@Override
	public Supplier<OutputStream> getOutputStreamSupplier(final String key) {
		return () -> handle(key, OUTPUT_HANDLER);
	}


	private <T> T handle(String key, Handler<T> handler) {

		RuntimeException lastRuntimeException = null;
		for (StreamLocator delegate : delegates) {
			try {
				T obj = handler.handle(key, delegate);
				if (obj != null) {
					return obj;
				}
			} catch (RuntimeException e) {
				lastRuntimeException = e;
			}
		}
		if (lastRuntimeException != null) {
			throw lastRuntimeException;
		}
		throw new RuntimeException("Resource not found: " + key);
	}

	@Override
	public Iterator<String> listKeys() {
		List<String> keys = new ArrayList<>();
		for (InputStreamSupplierLocator delegate : delegates) {
			Iterator<String> iter = delegate.listKeys();
			while (iter.hasNext()) {
				keys.add(iter.next());
			}
		}
		return keys.iterator();
	}

	private interface Handler<T> {
		T handle(String key, StreamLocator locator);
	}

	private static class InputHandler implements Handler<InputStream> {
		@Override
		public InputStream handle(String key, StreamLocator locator) {
			Supplier<? extends InputStream> supplier = locator.getInputStreamSupplier(key);
			return supplier.get();
		}
	}

	private static class OutputHandler implements Handler<OutputStream> {
		@Override
		public OutputStream handle(String key, StreamLocator locator) {
			Supplier<? extends OutputStream> outputSupplier = locator.getOutputStreamSupplier(key);
			return outputSupplier.get();
		}
	}


}
