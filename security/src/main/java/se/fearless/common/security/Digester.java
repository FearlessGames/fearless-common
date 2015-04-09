package se.fearless.common.security;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

/**
 * Digests strings
 */
public class Digester {
	private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

	private final String salt;
	private final Charset charset;

	public Digester(String salt) {
		this(salt, DEFAULT_CHARACTER_ENCODING);
	}

	public Digester(String salt, String encoding) {
		this.salt = salt;
		charset = Charset.forName(encoding);
	}

	public String sha512Hex(String s) {
		return getHash(s, Hashing.sha512()).toString();
	}

	public byte[] sha512(String s) {
		return getHash(s, Hashing.sha512()).asBytes();
	}

	public String md5Hex(String s) {
		return getHash(s, Hashing.md5()).toString();
	}

	private HashCode getHash(String s, HashFunction hashFunction) {
		return hashFunction.hashString(s + salt, charset);
	}

	public byte[] md5(String s) {
		return getHash(s, Hashing.md5()).asBytes();
	}
}
