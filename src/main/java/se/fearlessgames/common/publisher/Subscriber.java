package se.fearlessgames.common.publisher;

public interface Subscriber<T> {
	void update(T updated);
}

