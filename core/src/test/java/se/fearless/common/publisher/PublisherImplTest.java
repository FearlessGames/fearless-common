package se.fearless.common.publisher;

import com.googlecode.gentyref.TypeToken;
import org.junit.Test;

import static se.mockachino.Mockachino.*;


public class PublisherImplTest {

	private static final TypeToken<Subscriber<Integer>> INTEGER_SUBSCRIBER = new TypeToken<Subscriber<Integer>>() {
	};

	@Test
	public void subscribe() {
		SimplePublisher<Integer> intPublisher = new SimplePublisher<>();
		Subscriber<Integer> sub1 = mock(INTEGER_SUBSCRIBER);
		intPublisher.subscribe(sub1);

		intPublisher.updateSubscribers(3);

		verifyOnce().on(sub1).update(3);

		intPublisher.updateSubscribers(5);
		verifyOnce().on(sub1).update(3);
		verifyOnce().on(sub1).update(5);
	}

	@Test
	public void unsubscribe() {
		SimplePublisher<Integer> intPublisher = new SimplePublisher<>();
		Subscriber<Integer> sub1 = mock(INTEGER_SUBSCRIBER);
		Subscriber<Integer> sub2 = mock(INTEGER_SUBSCRIBER);
		intPublisher.subscribe(sub1);
		intPublisher.subscribe(sub2);

		intPublisher.updateSubscribers(3);

		intPublisher.unsubscribe(sub1);

		intPublisher.updateSubscribers(5);
		verifyOnce().on(sub1).update(3);
		verifyOnce().on(sub2).update(3);
		verifyNever().on(sub1).update(5);
		verifyOnce().on(sub2).update(5);
	}

}
