package se.fearless.common.uuid;

import se.fearless.common.time.SystemTimeProvider;
import se.fearless.common.time.TimeProvider;

import java.util.Calendar;
import java.util.Random;

public class UUIDFactoryImpl implements UUIDFactory {
	private final Random numberGenerator;
	private final TimeProvider timeProvider;

	public static final UUIDFactory INSTANCE = new UUIDFactoryImpl(new SystemTimeProvider(), new Random());

	public UUIDFactoryImpl(TimeProvider timeProvider, Random numberGenerator) {
		this.timeProvider = timeProvider;
		this.numberGenerator = numberGenerator;
	}

	/**
	 * Static factory to retrieve a type 4 (pseudo randomly generated) UUID.
	 * <p/>
	 * The <code>UUID</code> is generated using a cryptographically strong
	 * pseudo random number generator.
	 *
	 * @return a randomly generated <tt>UUID</tt>.
	 */
	@Override
	public UUID randomUUID() {
		byte[] randomBytes = new byte[16];
		numberGenerator.nextBytes(randomBytes);
		randomBytes[6] &= 0x0f;  /* clear version        */
		randomBytes[6] |= 0x40;  /* set to version 4     */
		randomBytes[8] &= 0x3f;  /* clear variant        */
		randomBytes[8] |= 0x80;  /* set to IETF variant  */
		return new UUID(randomBytes);
	}

	/**
	 * Static factory for creating a comb based random UUID
	 * The comb uuid differs from a regular random in that its less random over time so
	 * better optimized for db pk usage
	 * |-----random------------|day | current time in 1/300th of ms (sql server standard time resolution)
	 * ec38f5b7-6031-4079-bedd-9c7b012829e9
	 * byte[16] 12 of 32 is the same. 6 of 16 bytes
	 *
	 * @return the comb based <TT>UUID</TT>
	 */
	@Override
	public UUID combUUID() {
		byte[] uuidArray = randomUUID().getBytes();

		Calendar baseDate = Calendar.getInstance();
		baseDate.set(1900, 0, 1, 0, 0, 0);  //start the calendar at 1900-01-01 : 00:00:00

		Calendar now = Calendar.getInstance(); //current time
		now.setTimeInMillis(timeProvider.now());

		int days = (int) ((now.getTimeInMillis() - baseDate.getTimeInMillis()) / 1000 / 60d / 60d / 24d);

		long msecs = now.getTimeInMillis();

		byte[] daysArray = getBytes(days);
		byte[] msecsArray = getByts((long) (msecs / 3.333333d));	 // Note that SQL Server is accurate to 1/300th of a millisecond so we divide by 3.333333


		System.arraycopy(daysArray, daysArray.length - 2, uuidArray, uuidArray.length - 6, 2);
		System.arraycopy(msecsArray, msecsArray.length - 4, uuidArray, uuidArray.length - 4, 4);

		return new UUID(uuidArray);
	}

	private static byte[] getBytes(int value) {
		byte[] data = new byte[4];
		data[0] = (byte) ((byte) (value >>> 24) & 0xFF);
		data[1] = (byte) ((byte) (value >>> 16) & 0xFF);
		data[2] = (byte) ((byte) (value >>> 8) & 0xFF);
		data[3] = (byte) ((byte) (value) & 0xFF);
		return data;
	}


	private static byte[] getByts(long msecs) {
		byte[] buffer = new byte[8];
		UUID.writeLong(buffer, 0, msecs);
		return buffer;
	}
}
