package se.fearless.common.uuid;

import org.junit.Test;
import se.fearless.common.time.SystemTimeProvider;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;


public class UUIDTest {
	private final UUIDFactory uuidFactory = new UUIDFactoryImpl(new SystemTimeProvider(), new SecureRandom());

	@Test
	public void testString() {
		for (int i = 0; i < 100; i++) {
			UUID uuid = uuidFactory.randomUUID();
			String s = uuid.toString();
			UUID uuid2 = UUID.fromString(s);
			assertEquals(uuid, uuid2);
			assertEquals(s, uuid2.toString());
		}
	}
}
