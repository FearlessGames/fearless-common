package se.fearlessgames.common.lifetime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

public class LifetimeManagerImpl implements LifetimeManager {
	private final Collection<LifetimeListener> listeners;
	private final CountDownLatch startLatch = new CountDownLatch(1);
	private final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private State state = State.NOT_STARTED;

	public LifetimeManagerImpl() {
		this.listeners = new ArrayList<LifetimeListener>();
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public boolean isDead() {
		return state == State.SHUT_DOWN;
	}

	@Override
	public synchronized void addListener(LifetimeListener listener) {
		if (state == State.NOT_STARTED) {
			listeners.add(listener);
		} else {
			throw new IllegalStateException("Can't add listener to a running LifetimeManager");
		}
	}

	@Override
	public synchronized void start() {
		if (state == State.NOT_STARTED) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						waitForStart();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					shutdown();
				}
			});
			state = State.STARTING;
			for (LifetimeListener listener : listeners) {
				try {
					listener.onStart();
				} catch (Exception e) {
					// Ignore error to ensure that all listeners get the notification
				}
			}
			state = State.RUNNING;
			startLatch.countDown();
		}
	}

	@Override
	public synchronized void shutdown() {
		start();
		if (state == State.RUNNING) {
			state = State.SHUTTING_DOWN;
			for (LifetimeListener listener : listeners) {
				try {
					listener.onShutdown();
				} catch (Exception e) {
					// Ignore error to ensure that all listeners get the notification
				}
			}
			state = State.SHUT_DOWN;
			shutdownLatch.countDown();
			//interruptThreads();
		}
	}

	@Override
	public void waitForStart() throws InterruptedException {
		startLatch.await();
	}

	@Override
	public void waitForShutdown() throws InterruptedException {
		shutdownLatch.await();
	}
}
