package se.fearless.common.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ClasspathStreamLocatorTest {

	@Test
	public void findsInputStream() throws IOException {
		InputStreamSupplierLocator sl = new ClasspathStreamLocator();
		assertNotNull(sl.getInputStreamSupplier("/se/fearless/common/io/ClasspathStreamLocatorTest.class").get());
	}

	@Test(expected = RuntimeException.class)
	public void throwsExceptionWhenInputNotFound() throws IOException {
		InputStreamSupplierLocator sl = new ClasspathStreamLocator();
		sl.getInputStreamSupplier("notFound").get();
	}

	@Test(expected = RuntimeException.class)
	public void throwsExceptionForGetOutput() throws IOException {
		OutputStreamSupplierLocator sl = new ClasspathStreamLocator();
		sl.getOutputStreamSupplier("/se/fearless/common/io/ClasspathStreamLocatorTest.class").get();
	}
}
