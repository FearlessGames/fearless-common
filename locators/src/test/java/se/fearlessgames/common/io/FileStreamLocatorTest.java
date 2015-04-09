package se.fearlessgames.common.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class FileStreamLocatorTest {
	private static final String EXISTING_FILE = "existing.ext";
	private static final String NON_EXISTING_FILE = "nonExisting.ext";

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void findsInputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		StreamLocator sl = new FileStreamLocator(tempFolder.getRoot());

		sl.getInputSupplier(EXISTING_FILE).getInput();
	}

	@Test(expected=IOException.class)
	public void failsFindingInputStream() throws IOException {
		StreamLocator sl = new FileStreamLocator(tempFolder.getRoot());
		sl.getInputSupplier(NON_EXISTING_FILE).getInput();
	}

	@Test
	public void findsExistingOutputStream() throws IOException {
		tempFolder.newFile(EXISTING_FILE);
		StreamLocator sl = new FileStreamLocator(tempFolder.getRoot());

		sl.getOutputSupplier(EXISTING_FILE).getOutput();
	}

	@Test
	public void createsOutputStream() throws IOException {
		StreamLocator sl = new FileStreamLocator(tempFolder.getRoot());

		sl.getOutputSupplier(NON_EXISTING_FILE).getOutput();
	}

	@Test
	public void testListKeys() throws Exception {
		tempFolder.newFile(EXISTING_FILE);
		StreamLocator sl = new FileStreamLocator(tempFolder.getRoot());
		Iterator<String> iterator = sl.listKeys();
		Assert.assertTrue(iterator.hasNext());
		String fileName = iterator.next();
		Assert.assertEquals(EXISTING_FILE, fileName);
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testComplexListKeys() throws Exception {
		File folder = tempFolder.newFolder("folder");
		File oneLevelDown = new File(folder, "hello.txt");
		oneLevelDown.createNewFile();
		File subfolder = tempFolder.newFolder("folder/folder2");
		File twoLevelsDown = new File(subfolder, "hello.txt");
		twoLevelsDown.createNewFile();

		tempFolder.newFile(EXISTING_FILE);
		StreamLocator sl = new FileStreamLocator(tempFolder.getRoot());
		Iterator<String> iterator = sl.listKeys();
		Assert.assertTrue(iterator.hasNext());
		Collection<String> allKeys = new ArrayList<String>();
		while (iterator.hasNext()) {
			String s = iterator.next();
			allKeys.add(s);
		}
		Assert.assertEquals(3, allKeys.size());
		Assert.assertTrue(allKeys.contains("folder/hello.txt"));
		Assert.assertTrue(allKeys.contains("folder/folder2/hello.txt"));
		Assert.assertTrue(allKeys.contains(EXISTING_FILE));
		Assert.assertFalse(allKeys.contains(NON_EXISTING_FILE));


	}
}
