package se.fearlessgames.common.util.uuid;

import java.util.concurrent.atomic.AtomicLong;

public class UUIDMockFactory implements UUIDFactory {
	private final AtomicLong id = new AtomicLong(0);

	@Override
	public UUID randomUUID() {
		return new UUID(0, id.incrementAndGet());
	}

	@Override
	public UUID combUUID() {
		return randomUUID();
	}
}
