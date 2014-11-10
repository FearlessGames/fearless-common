package se.fearlessgames.common.uuid;

import org.junit.Test;
import se.fearlessgames.common.util.MockTimeProvider;
import se.fearlessgames.common.util.uuid.UUID;
import se.fearlessgames.common.util.uuid.UUIDFactory;
import se.fearlessgames.common.util.uuid.UUIDFactoryImpl;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class UUIDTest {
	private final MockTimeProvider timeProvider = new MockTimeProvider();
	private final UUIDFactory uuidFactory = new UUIDFactoryImpl(timeProvider, new SecureRandom());

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

	@Test
	public void combTest() throws InterruptedException {

		timeProvider.setNow(0);
		UUID firstUuid = uuidFactory.combUUID();
		UUID secondUuid = uuidFactory.combUUID();

		assertFalse(firstUuid.equals(secondUuid));
		long combPart1 = getCombPart(firstUuid);
		long combPart2 = getCombPart(secondUuid);
		assertEquals(combPart1, combPart2);

		timeProvider.advanceTime(20);

		UUID thirdUuid = uuidFactory.combUUID();
		long combPart3 = getCombPart(thirdUuid);
		assertFalse(combPart1 == combPart3);

	}

	private long getCombPart(UUID uuid) {
		String combString = "0x" + uuid.toString().substring(24, 36);
		return Long.decode(combString);
	}
}
