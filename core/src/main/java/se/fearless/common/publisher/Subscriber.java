package se.fearless.common.publisher;

public interface Subscriber<T> {
	void update(T updated);
}

