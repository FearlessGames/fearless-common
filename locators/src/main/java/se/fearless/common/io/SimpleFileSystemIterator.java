package se.fearless.common.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleFileSystemIterator {
	public Iterator<String> iterator(File root) {
		List<String> files = new ArrayList<>();
		if (!root.exists()) {
			return files.iterator();
		}

		fillUp(root, files, "");
		return files.iterator();
	}

	private void fillUp(File root, List<String> files, String path) {
		File[] children = root.listFiles();
		for (File file : children) {
			if (file.isDirectory()) {
				fillUp(file, files, path + file.getName() + "/");
			} else {
				files.add(path + file.getName());
			}
		}
	}
}
