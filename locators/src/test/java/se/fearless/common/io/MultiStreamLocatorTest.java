package se.fearless.common.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class MultiStreamLocatorTest {
	private static final String EXISTING_FILE = "existing.ext";
	private static final String NON_EXISTING_FILE = "nonExisting.ext";

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void findsInputStreamFromFilesystem() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		InputStreamSupplierLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()));

		assertNotNull(sl.getInputStreamSupplier(EXISTING_FILE).get());
	}

	@Test
	public void findsInputStreamFromClasspath() {
		InputStreamSupplierLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()), new ClasspathStreamLocator());
		assertNotNull(sl.getInputStreamSupplier("/se/fearless/common/io/MultiStreamLocator.class").get());
	}

	@Test(expected = RuntimeException.class)
	public void failsFindingInputStream() {
		InputStreamSupplierLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()));
		sl.getInputStreamSupplier(NON_EXISTING_FILE).get();
	}

	@Test
	public void findsExistingOutputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		OutputStreamSupplierLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()));

		assertNotNull(sl.getOutputStreamSupplier(EXISTING_FILE).get());
	}

	@Test
	public void createsOutputStream() {
		OutputStreamSupplierLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()));

		assertNotNull(sl.getOutputStreamSupplier(NON_EXISTING_FILE).get());
	}
}
