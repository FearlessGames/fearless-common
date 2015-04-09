package se.fearlessgames.common.lifetime;

import org.junit.Before;
import org.junit.Test;

import static se.mockachino.Mockachino.*;

public class LifetimeManagerImplTest {
	private LifetimeManagerImpl lifetimeManager;

	@Before
	public void setup() {
		lifetimeManager = new LifetimeManagerImpl();
	}

	@Test
	public void start() {
		LifetimeListener listener = Mockachino.mock(LifetimeListener.class);
		lifetimeManager.addListener(listener);

		lifetimeManager.start();

		Mockachino.verifyOnce().on(listener).onStart();
	}

	@Test
	public void shutdown() {
		LifetimeListener listener = Mockachino.mock(LifetimeListener.class);
		lifetimeManager.addListener(listener);

		lifetimeManager.start();

		Mockachino.verifyOnce().on(listener).onStart();

		lifetimeManager.shutdown();

		Mockachino.verifyOnce().on(listener).onShutdown();
	}



}
