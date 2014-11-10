package se.fearlessgames.common.collections;

import com.google.common.base.Function;
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
	public void sumIntegerList() throws Exception {
		List<Integer> ints = Lists.newArrayList(2, 1, 3, 4);
		int sum = Collections3.sum(ints, new Function<Integer, Integer>(){
			@Override
			public Integer apply(Integer input) {
				return input;
			}
		});
		assertEquals(10, sum);
	}

	@Test
	public void sumObjects() throws Exception {
		Set<Foo> foo = Sets.newHashSet(new Foo(2), new Foo(3), new Foo(5), new Foo(1));
		int sum = Collections3.sum(foo, new Function<Foo, Integer>() {
			@Override
			public Integer apply(Foo input) {
				return input.getValue();
			}
		});
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
