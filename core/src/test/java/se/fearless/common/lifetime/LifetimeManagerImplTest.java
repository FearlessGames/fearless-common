package se.fearless.common.lifetime;

import org.junit.Before;
import org.junit.Test;

import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verifyOnce;

public class LifetimeManagerImplTest {
	private LifetimeManagerImpl lifetimeManager;

	@Before
	public void setup() {
		lifetimeManager = new LifetimeManagerImpl();
	}

	@Test
	public void start() {
		LifetimeListener listener = mock(LifetimeListener.class);
		lifetimeManager.addListener(listener);

		lifetimeManager.start();

		verifyOnce().on(listener).onStart();
	}

	@Test
	public void shutdown() {
		LifetimeListener listener = mock(LifetimeListener.class);
		lifetimeManager.addListener(listener);

		lifetimeManager.start();

		verifyOnce().on(listener).onStart();

		lifetimeManager.shutdown();

		verifyOnce().on(listener).onShutdown();
	}



}
