package se.fearlessgames.common.mock;

import se.mockachino.Mockachino;
import se.mockachino.Settings;

public class MockUtil {
	public static <T> T deepMock(Class<T> clazz) {
		return Mockachino.mock(clazz, Settings.fallback(Mockachino.DEEP_MOCK));
	}
}
