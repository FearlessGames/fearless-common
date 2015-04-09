package se.fearlessgames.common.collections;

import java.util.List;
import java.util.Random;

public class Collections3 {

	public static <T> T getRandomElement(List<T> list, Random rand) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(rand.nextInt(list.size()));
	}

	public static <T> T removeRandomElement(List<T> list, Random rand) {
		if (list.isEmpty()) {
			return null;
		}
		return list.remove(rand.nextInt(list.size()));
	}

	public static <T> long sum(Iterable<T> iterable, Function<T, Integer> elementValueFunction) {
		long sum = 0;
		for (T t : iterable) {
			sum += elementValueFunction.apply(t);
		}
		return sum;
	}

	public interface Function<F, T> {
		T apply(F from);
	}
}
