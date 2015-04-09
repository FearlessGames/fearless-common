package se.fearless.common.lifetime;

public interface LifetimeManager {
	public enum State {
		NOT_STARTED, STARTING, RUNNING, SHUTTING_DOWN, SHUT_DOWN
	}

	void addListener(LifetimeListener listener);

	void start();

	void shutdown();

	void waitForStart() throws InterruptedException;

	void waitForShutdown() throws InterruptedException;

	State getState();

	boolean isDead();
}
