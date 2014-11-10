package se.fearlessgames.common.publisher;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SimplePublisher<T> implements Publisher<T> {
	private final Set<Subscriber<T>> subscribers = Sets.newCopyOnWriteArraySet();

	@Override
	public void subscribe(Subscriber<T> subscriber) {
		subscribers.add(subscriber);
	}

	@Override
	public void unsubscribe(Subscriber<T> subscriber) {
		subscribers.remove(subscriber);
	}

	public void updateSubscribers(T argument) {
		for (Subscriber<T> subscriber : subscribers) {
			subscriber.update(argument);
		}
	}
}
