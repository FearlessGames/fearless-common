package se.fearless.common.time;

public class SystemTimeProvider implements TimeProvider {
	@Override
	public long now() {
		return System.currentTimeMillis();
	}

	@Override
	public void sleep(long timeInMilliSeconds) throws InterruptedException {
		Thread.sleep(timeInMilliSeconds);
	}
}
