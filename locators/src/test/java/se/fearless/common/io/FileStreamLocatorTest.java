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

public class FileStreamLocatorTest {
	private static final String EXISTING_FILE = "existing.ext";
	private static final String NON_EXISTING_FILE = "nonExisting.ext";

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void findsInputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		InputStreamSupplierLocator sl = new FileStreamLocator(tempFolder.getRoot());

		assertNotNull(sl.getInputStreamSupplier(EXISTING_FILE).get());
	}

	@Test(expected = RuntimeException.class)
	public void failsFindingInputStream() {
		InputStreamSupplierLocator sl = new FileStreamLocator(tempFolder.getRoot());
		sl.getInputStreamSupplier(NON_EXISTING_FILE).get();
	}

	@Test
	public void findsExistingOutputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		OutputStreamSupplierLocator sl = new FileStreamLocator(tempFolder.getRoot());

		assertNotNull(sl.getOutputStreamSupplier(EXISTING_FILE).get());
	}

	@Test
	public void createsOutputStream() {
		OutputStreamSupplierLocator sl = new FileStreamLocator(tempFolder.getRoot());

		assertNotNull(sl.getOutputStreamSupplier(NON_EXISTING_FILE).get());
	}

	@Test
	public void testListKeys() throws Exception {
		tempFolder.newFile(EXISTING_FILE);
		InputStreamSupplierLocator sl = new FileStreamLocator(tempFolder.getRoot());
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
		InputStreamSupplierLocator sl = new FileStreamLocator(tempFolder.getRoot());
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
