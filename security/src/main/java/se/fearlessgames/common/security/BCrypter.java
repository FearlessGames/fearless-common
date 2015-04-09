package se.fearlessgames.common.security;

import org.mindrot.jbcrypt.BCrypt;

public class BCrypter {
	private static final int BCRYPT_NUMBER_OF_ROUNDS = 10;

	public static String bcrypt(String s, String salt) {
		return BCrypt.hashpw(s, salt);
	}

	public static boolean matchesBCrypt(String candidate, String hash) {
		return BCrypt.checkpw(candidate, hash);
	}

	public static String generateBCryptSalt() {
		return BCrypt.gensalt(BCRYPT_NUMBER_OF_ROUNDS);
	}
}
