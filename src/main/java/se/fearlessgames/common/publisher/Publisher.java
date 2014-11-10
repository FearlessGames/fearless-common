package se.fearlessgames.common.publisher;

public interface Publisher<T> {
	void subscribe(Subscriber<T> subscriber);
	void unsubscribe(Subscriber<T> subscriber);
}
