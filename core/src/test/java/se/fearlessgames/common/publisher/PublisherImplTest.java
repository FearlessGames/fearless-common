package se.fearlessgames.common.publisher;

import com.googlecode.gentyref.TypeToken;
import org.junit.Test;

import static se.mockachino.Mockachino.*;


public class PublisherImplTest {

	private static final TypeToken<Subscriber<Integer>> INTEGER_SUBSCRIBER = new TypeToken<Subscriber<Integer>>() {
	};

	@Test
	public void subscribe() {
		SimplePublisher<Integer> intPublisher = new SimplePublisher<Integer>();
		Subscriber<Integer> sub1 = Mockachino.mock(INTEGER_SUBSCRIBER);
		intPublisher.subscribe(sub1);

		intPublisher.updateSubscribers(3);

		Mockachino.verifyOnce().on(sub1).update(3);

		intPublisher.updateSubscribers(5);
		Mockachino.verifyOnce().on(sub1).update(3);
		Mockachino.verifyOnce().on(sub1).update(5);
	}

	@Test
	public void unsubscribe() {
		SimplePublisher<Integer> intPublisher = new SimplePublisher<Integer>();
		Subscriber<Integer> sub1 = Mockachino.mock(INTEGER_SUBSCRIBER);
		Subscriber<Integer> sub2 = Mockachino.mock(INTEGER_SUBSCRIBER);
		intPublisher.subscribe(sub1);
		intPublisher.subscribe(sub2);

		intPublisher.updateSubscribers(3);

		intPublisher.unsubscribe(sub1);

		intPublisher.updateSubscribers(5);
		Mockachino.verifyOnce().on(sub1).update(3);
		Mockachino.verifyOnce().on(sub2).update(3);
		Mockachino.verifyNever().on(sub1).update(5);
		Mockachino.verifyOnce().on(sub2).update(5);
	}

}
