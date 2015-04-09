package se.fearlessgames.common.security;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DigesterTest {
	private static final String digestHelloWorld = "949b6454e366b5993126e1a1cbd6e694c95656c9bb10949f6c8757d180ef1f8cea904db870783aa68963ca2659bc9ce169285b91a6ab4a2ba2d80008c19a5106";
	private static final String md5HelloWorld = "35e40910d0cdc77285549cd56cba5423";
	public static final String rounds12helloworldbcrypt = "$2a$12$7ELpIQHWwJNMnGzJeK6U4ObUt623cjO53AEGGzDL9P03M5ZU.u98W";

	@Test
	public void testSha256() {
		Digester digester = new Digester("spaced");
		String digest = digester.sha512Hex("Hello world");
		assertEquals(digestHelloWorld, digest);

		digest = digester.sha512Hex("Hello world");
		assertEquals(digestHelloWorld, digest);

		digest = digester.sha512Hex("Hello world");
		assertEquals(digestHelloWorld, digest);

	}

	@Test
	public void testMd5() {
		Digester digester = new Digester("spaced");
		String digest = digester.md5Hex("Hello world");
		assertEquals(md5HelloWorld, digest);
		digest = digester.md5Hex("Hello world");
		assertEquals(md5HelloWorld, digest);
	}

}
