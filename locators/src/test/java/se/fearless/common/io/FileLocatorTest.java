package se.fearless.common.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class FileLocatorTest {
	private static final String EXISTING_FILE = "existing.ext";
	private static final String NON_EXISTING_FILE = "nonExisting.ext";

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void findsInputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		ByteSourceLocator sl = new FileLocator(tempFolder.getRoot());

		assertNotNull(sl.getByteSource(EXISTING_FILE).openStream());
	}

	@Test(expected = IOException.class)
	public void failsFindingInputStream() throws IOException {
		ByteSourceLocator sl = new FileLocator(tempFolder.getRoot());
		sl.getByteSource(NON_EXISTING_FILE).openStream();
	}

	@Test
	public void findsExistingOutputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		ByteSinkLocator sl = new FileLocator(tempFolder.getRoot());

		assertNotNull(sl.getByteSink(EXISTING_FILE).openStream());
	}

	@Test
	public void createsOutputStream() throws IOException {
		ByteSinkLocator sl = new FileLocator(tempFolder.getRoot());

		assertNotNull(sl.getByteSink(NON_EXISTING_FILE).openStream());
	}

	@Test
	public void testListKeys() throws Exception {
		tempFolder.newFile(EXISTING_FILE);
		ByteSourceLocator sl = new FileLocator(tempFolder.getRoot());
		Iterator<String> iterator = sl.listKeys();
		assertTrue(iterator.hasNext());
		String fileName = iterator.next();
		assertEquals(EXISTING_FILE, fileName);
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testComplexListKeys() throws Exception {
		File folder = tempFolder.newFolder("folder");
		File oneLevelDown = new File(folder, "hello.txt");
		oneLevelDown.createNewFile();
		File subfolder = tempFolder.newFolder("folder", "folder2");
		File twoLevelsDown = new File(subfolder, "hello.txt");
		twoLevelsDown.createNewFile();

		tempFolder.newFile(EXISTING_FILE);
		ByteSourceLocator sl = new FileLocator(tempFolder.getRoot());
		Iterator<String> iterator = sl.listKeys();
		assertTrue(iterator.hasNext());
		Collection<String> allKeys = new ArrayList<>();
		while (iterator.hasNext()) {
			String s = iterator.next();
			allKeys.add(s);
		}
		assertEquals(3, allKeys.size());
		assertTrue(allKeys.contains("folder/hello.txt"));
		assertTrue(allKeys.contains("folder/folder2/hello.txt"));
		assertTrue(allKeys.contains(EXISTING_FILE));
		assertFalse(allKeys.contains(NON_EXISTING_FILE));


	}
}
