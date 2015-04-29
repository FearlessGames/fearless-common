package se.fearless.common.security;

public interface Encrypter {
	String encrypt(String plainText, String password);

	String decrypt(String cryptoText, String password);
}
