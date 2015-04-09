package se.fearless.common.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class MultiStreamLocatorTest {
	private static final String EXISTING_FILE = "existing.ext";
	private static final String NON_EXISTING_FILE = "nonExisting.ext";

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void findsInputStreamFromFilesystem() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		StreamLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()));

		sl.getInputSupplier(EXISTING_FILE).getInput();
	}

	@Test
	public void findsInputStreamFromClasspath() throws IOException {
		StreamLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()), new ClasspathStreamLocator());
		sl.getInputSupplier("/se/fearlessgames/common/io/MultiStreamLocator.class").getInput();
	}

	@Test(expected=IOException.class)
	public void failsFindingInputStream() throws IOException {
		StreamLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()));
		sl.getInputSupplier(NON_EXISTING_FILE).getInput();
	}

	@Test
	public void findsExistingOutputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		StreamLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()));

		sl.getOutputSupplier(EXISTING_FILE).getOutput();
	}

	@Test
	public void createsOutputStream() throws IOException {
		StreamLocator sl = new MultiStreamLocator(new FileStreamLocator(tempFolder.getRoot()));

		sl.getOutputSupplier(NON_EXISTING_FILE).getOutput();
	}
}
