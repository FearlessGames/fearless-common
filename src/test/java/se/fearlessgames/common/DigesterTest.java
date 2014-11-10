package se.fearlessgames.common;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import se.fearlessgames.common.util.Digester;
import se.fearlessgames.common.util.uuid.UUIDFactoryImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

	@Test
	public void testSpacedLogin() {
		Server server = new Server();
		Salts salts = server.getUserSalts("Dem");

		String bchash = BCrypt.hashpw("mittpass", salts.userSalt);
		Digester digester = new Digester("spaced");
		String hash = digester.sha512Hex(bchash + salts.oneTimeSalt);
		boolean auth = server.authUser("Dem", hash);
		assertTrue(auth);
	}

	private static class Server {
		private final Map<String, String> userSaltDB;
		private final Map<String, String> userHashDB;
		private final Map<String, String> oneTimeSaltDB;

		private Server() {
			userHashDB = new HashMap<String, String>();
			userSaltDB = new HashMap<String, String>();
			oneTimeSaltDB = new HashMap<String, String>();

			addUser("Dem", "mittpass");
		}

		private void addUser(String user, String password) {
			userSaltDB.put(user, BCrypt.gensalt(5));
			userHashDB.put(user, BCrypt.hashpw(password, userSaltDB.get(user)));
		}

		public Salts getUserSalts(String userName) {
			String userSalt = userSaltDB.get(userName);
			String oneTimeSalt = UUIDFactoryImpl.INSTANCE.randomUUID().toString();
			oneTimeSaltDB.put(userName, oneTimeSalt);
			return new Salts(userSalt, oneTimeSalt);
		}

		public boolean authUser(String user, String hash) {
			Digester digester = new Digester("spaced");
			String serverHash = digester.sha512Hex(userHashDB.get(user) + oneTimeSaltDB.remove(user));
			return serverHash.equals(hash);
		}
	}

	private static class Salts {
		private Salts(String userSalt, String oneTimeSalt) {
			this.userSalt = userSalt;
			this.oneTimeSalt = oneTimeSalt;
		}

		private final String userSalt;
		private final String oneTimeSalt;
	}

}
