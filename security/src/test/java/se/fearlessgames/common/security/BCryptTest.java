package se.fearlessgames.common.security;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import se.fearlessgames.common.uuid.UUIDFactoryImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class BCryptTest {

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
