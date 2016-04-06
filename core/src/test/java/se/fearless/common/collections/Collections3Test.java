package se.fearless.common.collections;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;


public class Collections3Test {
	@Test
	public void testGetRandomElement() throws Exception {
		List<String> strings = Lists.newArrayList("krka", "is", "foo", "bar");

		Random random = new Random();
		for(int i = 0; i < 1000; i++) {
			String result = Collections3.getRandomElement(strings, random);
			assertTrue(strings.contains(result));
		}
	}

	@Test
	public void testGetRandomElementForEmptyList() throws Exception {
		List<String> strings = Lists.newArrayList();

		Random random = new Random();
		for(int i = 0; i < 1000; i++) {
			String result = Collections3.getRandomElement(strings, random);
			assertNull(result);
		}
	}

	@Test
	public void removeRandomElement() throws Exception {
		List<String> strings = Lists.newArrayList("a", "b", "c", "d", "e");

		Random random = new Random();

		List<String> removedStrings = Lists.newArrayList();
		for (int i = 0; i < 5; i++) {
			String removedString = Collections3.removeRandomElement(strings, random);
			removedStrings.add(removedString);
		}
		assertEquals(5, removedStrings.size());
		assertTrue(removedStrings.contains("a"));
		assertTrue(removedStrings.contains("b"));
		assertTrue(removedStrings.contains("c"));
		assertTrue(removedStrings.contains("d"));
		assertTrue(removedStrings.contains("e"));
	}


	@Test
	public void removeRandomElementReturnsNullAfterAllElementsAreDepleted() throws Exception {
		List<String> strings = Lists.newArrayList("a", "b");

		Random random = new Random();

		Collections3.removeRandomElement(strings, random);
		Collections3.removeRandomElement(strings, random);

		String removedElement = Collections3.removeRandomElement(strings, random);
		assertNull(removedElement);
	}

	@Test
	public void sumIntegerList() throws Exception {
		List<Integer> ints = Lists.newArrayList(2, 1, 3, 4);
		long sum = Collections3.sum(ints, input -> input);
		assertEquals(10, sum);
	}

	@Test
	public void sumObjects() throws Exception {
		Set<Foo> foo = Sets.newHashSet(new Foo(2), new Foo(3), new Foo(5), new Foo(1));
		long sum = Collections3.sum(foo, Foo::getValue);
		assertEquals(11, sum);
	}

	private class Foo {
		private final int value;

		public Foo(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
