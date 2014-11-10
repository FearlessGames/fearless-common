package se.fearlessgames.common.util;

public interface TimeProvider {
	long now();

	void sleep(long timeInMilliSeconds) throws InterruptedException;
}
