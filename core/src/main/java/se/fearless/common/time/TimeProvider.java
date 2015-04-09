package se.fearless.common.time;

public interface TimeProvider {
	long now();

	void sleep(long timeInMilliSeconds) throws InterruptedException;
}
