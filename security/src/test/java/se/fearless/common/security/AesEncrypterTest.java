package se.fearless.common.security;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AesEncrypterTest {

	private Encrypter encrypter;

	@Before
	public void setUp() throws Exception {
		encrypter = new AesEncrypter();
	}

	@Test
	public void encryptionMakesThePlainTextDifferent() throws Exception {
		String plainText = "hiflyer";
		String password = "isAwesome";
		String encrypted = encrypter.encrypt(plainText, password);
		String decrypted = encrypter.decrypt(encrypted, password);

		assertEquals(plainText, decrypted);
	}

	@Test
	public void encryptAndDecryptShortString() throws Exception {
		String plainText = "hiflyer";
		String password = "isAwesome";
		String encrypted = encrypter.encrypt(plainText, password);
		String decrypted = encrypter.decrypt(encrypted, password);

		assertEquals(plainText, decrypted);
	}
}