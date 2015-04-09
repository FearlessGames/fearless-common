package se.fearless.common.io;

import com.google.common.collect.Iterators;

import java.io.File;
import java.util.Iterator;
import java.util.Stack;

public class FileSystemIterator implements Iterator<String> {
	private Stack<Iterator<File>> stack;

	public FileSystemIterator(File root) {
		stack = new Stack<Iterator<File>>();
		stack.push(Iterators.forArray(root.listFiles()));
	}

	@Override
	public boolean hasNext() {
		if (stack.empty()) {
			return false;
		}
		return stack.peek().hasNext();
	}

	@Override
	public String next() {
		if (stack.empty()) {
			return null;
		}
		Iterator<File> top = stack.peek();
		while (top != null && !top.hasNext()) {
			top = stack.pop();
		}
		if (top == null) {
			return null;
		}
		return top.next().getName();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported");
	}
}
