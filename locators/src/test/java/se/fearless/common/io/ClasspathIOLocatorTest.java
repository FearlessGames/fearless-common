package se.fearless.common.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ClasspathIOLocatorTest {

	@Test
	public void findsInputStream() throws IOException {
		ByteSourceLocator sl = new ClasspathIOLocator();
		assertNotNull(sl.getByteSource("/se/fearless/common/io/ClasspathStreamLocatorTest.class").openStream());
	}

	@Test(expected = RuntimeException.class)
	public void throwsExceptionWhenInputNotFound() throws IOException {
		ByteSourceLocator sl = new ClasspathIOLocator();
		sl.getByteSource("notFound").openStream();
	}

	@Test(expected = RuntimeException.class)
	public void throwsExceptionForGetOutput() throws IOException {
		ByteSinkLocator sl = new ClasspathIOLocator();
		sl.getByteSink("/se/fearless/common/io/ClasspathStreamLocatorTest.class").openStream();
	}
}
