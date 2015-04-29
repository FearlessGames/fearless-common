package se.fearless.common.security;

import com.google.common.io.BaseEncoding;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AesEncrypter implements Encrypter {

	private final Cipher cipher;
	private Charset utf8;

	public AesEncrypter() {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			utf8 = Charset.forName("UTF-8");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String encrypt(String plainText, String password) {
		try {
			byte[] encryptedBytes = encrypt(getKey(password), plainText.getBytes(utf8));
			return BaseEncoding.base64().encode(encryptedBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(String cryptoText, String password) {
		try {
			byte[] decodedBytes = BaseEncoding.base64().decode(cryptoText);
			byte[] decryptedBytes = decrypt(getKey(password), decodedBytes);
			return new String(decryptedBytes, utf8);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] decrypt(byte[] key, byte[] encrypted) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			return cipher.doFinal(encrypted);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] encrypt(byte[] key, byte[] clear) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return cipher.doFinal(clear);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] getKey(String password) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(password.getBytes(utf8));
		keyGenerator.init(128, sr);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}
}
