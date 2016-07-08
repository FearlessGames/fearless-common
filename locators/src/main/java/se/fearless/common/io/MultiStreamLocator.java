package se.fearless.common.io;

import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiStreamLocator implements IOLocator {
	private static final InputHandler INPUT_HANDLER = new InputHandler();
	private static final OutputHandler OUTPUT_HANDLER = new OutputHandler();

	private final IOLocator[] delegates;

	public MultiStreamLocator(IOLocator... delegates) {
		this.delegates = delegates;
	}

	@Override
	public ByteSource getByteSource(final String key) {
		return new ByteSource() {
			@Override
			public InputStream openStream() throws IOException {
				return handle(key, INPUT_HANDLER);
			}
		};
	}

	@Override
	public ByteSink getByteSink(final String key) {
		return new ByteSink() {
			@Override
			public OutputStream openStream() throws IOException {
				return handle(key, OUTPUT_HANDLER);
			}
		};
	}


	private <T> T handle(String key, Handler<T> handler) throws IOException {
		IOException lastIOException = null;
		RuntimeException lastRuntimeException = null;
		for (IOLocator delegate : delegates) {
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
		List<String> keys = new ArrayList<>();
		for (ByteSourceLocator delegate : delegates) {
			Iterator<String> iter = delegate.listKeys();
			while (iter.hasNext()) {
				keys.add(iter.next());
			}
		}
		return keys.iterator();
	}

	private interface Handler<T> {
		T handle(String key, IOLocator locator) throws IOException;
	}

	private static class InputHandler implements Handler<InputStream> {
		@Override
		public InputStream handle(String key, IOLocator locator) throws IOException {
			ByteSource byteSource = locator.getByteSource(key);
			return byteSource.openStream();
		}
	}

	private static class OutputHandler implements Handler<OutputStream> {
		@Override
		public OutputStream handle(String key, IOLocator locator) throws IOException {
			ByteSink outputSupplier = locator.getByteSink(key);
			return outputSupplier.openStream();
		}
	}


}
