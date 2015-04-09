package se.fearlessgames.common.io;

import org.junit.Test;

import java.io.IOException;

public class ClasspathStreamLocatorTest {

	@Test
	public void findsInputStream() throws IOException {
		StreamLocator sl = new ClasspathStreamLocator();
		sl.getInputSupplier("/se/fearlessgames/common/io/ClasspathStreamLocatorTest.class").getInput();
	}

	@Test(expected=IOException.class)
	public void throwsExceptionWhenInputNotFound() throws IOException {
		StreamLocator sl = new ClasspathStreamLocator();
		sl.getInputSupplier("notFound").getInput();
	}

	@Test(expected=IOException.class)
	public void throwsExceptionForGetOutput() throws IOException {
		StreamLocator sl = new ClasspathStreamLocator();
		sl.getOutputSupplier("/se/fearlessgames/common/io/ClasspathStreamLocatorTest.class").getOutput();
	}
}
