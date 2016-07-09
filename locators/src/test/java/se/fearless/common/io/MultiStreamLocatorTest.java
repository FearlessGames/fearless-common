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
		ByteSourceLocator sl = new MultiStreamLocator(new FileLocator(tempFolder.getRoot()));

		assertNotNull(sl.getByteSource(EXISTING_FILE).openStream());
	}

	@Test
	public void findsInputStreamFromClasspath() throws IOException {
		ByteSourceLocator sl = new MultiStreamLocator(new FileLocator(tempFolder.getRoot()), new ClasspathIOLocator());
		assertNotNull(sl.getByteSource("/se/fearless/common/io/MultiStreamLocator.class").openStream());
	}

	@Test(expected = IOException.class)
	public void failsFindingInputStream() throws IOException {
		ByteSourceLocator sl = new MultiStreamLocator(new FileLocator(tempFolder.getRoot()));
		sl.getByteSource(NON_EXISTING_FILE).openStream();
	}

	@Test
	public void findsExistingOutputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		ByteSinkLocator sl = new MultiStreamLocator(new FileLocator(tempFolder.getRoot()));

		assertNotNull(sl.getByteSink(EXISTING_FILE).openStream());
	}

	@Test
	public void createsOutputStream() throws IOException {
		ByteSinkLocator sl = new MultiStreamLocator(new FileLocator(tempFolder.getRoot()));

		assertNotNull(sl.getByteSink(NON_EXISTING_FILE).openStream());
	}
}
