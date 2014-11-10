package se.fearlessgames.common.util;

public class MockTimeProvider implements TimeProvider {

	private long now;

	public void setNow(long now) {
		this.now = now;
	}

	public void advanceTime(long amountInMillis) {
		now += amountInMillis;
	}

	@Override
	public long now() {
		return now;
	}

	@Override
	public void sleep(long timeInMilliSeconds) {
		now += timeInMilliSeconds;
	}
}
