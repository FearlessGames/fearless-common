package se.fearless.common.uuid;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class UUID implements Serializable, Comparable<UUID> {

	public static final UUID ZERO = new UUID(0, 0);
	/**
	 *
	 */
	private static final long serialVersionUID = 1396491779628510435L;

	/*
	 * The most significant 64 bits of this UUID.
	 *
	 * @serial
	 */
	private final long mostSigBits;

	/*
	 * The least significant 64 bits of this UUID.
	 *
	 * @serial
	 */
	private final long leastSigBits;

	private final int hashCode;

	UUID(byte[] data) {
		long msb = 0;
		long lsb = 0;
		assert data.length == 16;
		for (int i = 0; i < 8; i++) {
			msb = (msb << 8) | (data[i] & 0xff);
		}
		for (int i = 8; i < 16; i++) {
			lsb = (lsb << 8) | (data[i] & 0xff);
		}
		this.mostSigBits = msb;
		this.leastSigBits = lsb;
		this.hashCode = calculateHashCode();
	}

	/**
	 * Constructs a new <tt>UUID</tt> using the specified data.
	 * <tt>mostSigBits</tt> is used for the most significant 64 bits
	 * of the <tt>UUID</tt> and <tt>leastSigBits</tt> becomes the
	 * least significant 64 bits of the <tt>UUID</tt>.
	 *
	 * @param mostSigBits
	 * @param leastSigBits
	 */
	public UUID(long mostSigBits, long leastSigBits) {
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
		this.hashCode = calculateHashCode();
	}


	/**
	 * Static factory to retrieve a type 3 (name based) <tt>UUID</tt> based on
	 * the specified byte array.
	 *
	 * @param name a byte array to be used to construct a <tt>UUID</tt>.
	 * @return a <tt>UUID</tt> generated from the specified array.
	 */
	public static UUID nameUUIDFromBytes(byte[] name) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			throw new InternalError("MD5 not supported");
		}
		byte[] md5Bytes = md.digest(name);
		md5Bytes[6] &= 0x0f;  /* clear version        */
		md5Bytes[6] |= 0x30;  /* set to version 3     */
		md5Bytes[8] &= 0x3f;  /* clear variant        */
		md5Bytes[8] |= 0x80;  /* set to IETF variant  */
		return new UUID(md5Bytes);
	}

	/**
	 * Creates a <tt>UUID</tt> from the string standard representation as
	 * described in the {@link #toString} method.
	 *
	 * @param name a string that specifies a <tt>UUID</tt>.
	 * @return a <tt>UUID</tt> with the specified value.
	 * @throws IllegalArgumentException if name does not conform to the
	 *                                  string representation as described in {@link #toString}.
	 */
	public static UUID fromString(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Invalid UUID string: null");
		}
		String[] components = name.split("-");
		if (components.length != 5) {
			throw new IllegalArgumentException("Invalid UUID string: " + name);
		}
		for (int i = 0; i < 5; i++) {
			components[i] = "0x" + components[i];
		}

		long mostSigBits = Long.decode(components[0]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]).longValue();

		long leastSigBits = Long.decode(components[3]).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]).longValue();

		return new UUID(mostSigBits, leastSigBits);
	}

	// Field Accessor Methods

	/**
	 * Returns the least significant 64 bits of this UUID's 128 bit value.
	 *
	 * @return the least significant 64 bits of this UUID's 128 bit value.
	 */
	public long getLeastSignificantBits() {
		return leastSigBits;
	}

	/**
	 * Returns the most significant 64 bits of this UUID's 128 bit value.
	 *
	 * @return the most significant 64 bits of this UUID's 128 bit value.
	 */
	public long getMostSignificantBits() {
		return mostSigBits;
	}


	// Object Inherited Methods

	/**
	 * Returns a <code>String</code> object representing this
	 * <code>UUID</code>.
	 * <p/>
	 * <p>The UUID string representation is as described by this BNF :
	 * <blockquote><pre>
	 * {@code
	 * UUID						 = <time_low> "-" <time_mid> "-"
	 *                          <time_high_and_version> "-"
	 *                          <variant_and_sequence> "-"
	 *                          <node>
	 * time_low               = 4*<hexOctet>
	 * time_mid               = 2*<hexOctet>
	 * time_high_and_version  = 2*<hexOctet>
	 * variant_and_sequence   = 2*<hexOctet>
	 * node                   = 6*<hexOctet>
	 * hexOctet               = <hexDigit><hexDigit>
	 * hexDigit               =
	 *       "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
	 *       | "a" | "b" | "c" | "d" | "e" | "f"
	 *       | "A" | "B" | "C" | "D" | "E" | "F"
	 * }</pre></blockquote>
	 *
	 * @return a string representation of this <tt>UUID</tt>.
	 */
	@Override
	public String toString() {
		return (digits(mostSigBits >> 32, 8) + "-" +
				digits(mostSigBits >> 16, 4) + "-" +
				digits(mostSigBits, 4) + "-" +
				digits(leastSigBits >> 48, 4) + "-" +
				digits(leastSigBits, 12));
	}

	/**
	 * Returns val represented by the specified number of hex digits.
	 */
	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	/**
	 * Returns a hash code for this <code>UUID</code>.
	 *
	 * @return a hash code value for this <tt>UUID</tt>.
	 */
	@Override
	public int hashCode() {
		return hashCode;
	}

	private int calculateHashCode() {
		return (int) ((mostSigBits >> 32) ^
				mostSigBits ^
				(leastSigBits >> 32) ^
				leastSigBits);
	}

	/**
	 * Compares this object to the specified object.  The result is
	 * <tt>true</tt> if and only if the argument is not
	 * <tt>null</tt>, is a <tt>UUID</tt> object, has the same variant,
	 * and contains the same value, bit for bit, as this <tt>UUID</tt>.
	 *
	 * @param obj the object to compare with.
	 * @return <code>true</code> if the objects are the same;
	 *         <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UUID)) {
			return false;
		}
		UUID id = (UUID) obj;
		return (mostSigBits == id.mostSigBits &&
				leastSigBits == id.leastSigBits);
	}

	// Comparison Operations

	/**
	 * Compares this UUID with the specified UUID.
	 * <p/>
	 * <p>The first of two UUIDs follows the second if the most significant
	 * field in which the UUIDs differ is greater for the first UUID.
	 *
	 * @param val <tt>UUID</tt> to which this <tt>UUID</tt> is to be compared.
	 * @return -1, 0 or 1 as this <tt>UUID</tt> is less than, equal
	 *         to, or greater than <tt>val</tt>.
	 */
	@Override
	public int compareTo(UUID val) {
		// The ordering is intentionally set up so that the UUIDs
		// can simply be numerically compared as two numbers
		return (this.mostSigBits < val.mostSigBits ? -1 :
				(this.mostSigBits > val.mostSigBits ? 1 :
						(this.leastSigBits < val.leastSigBits ? -1 :
								(this.leastSigBits > val.leastSigBits ? 1 :
										0))));
	}


	public byte[] getBytes() {
		byte[] buffer = new byte[8 * 2];
		writeLong(buffer, 0, mostSigBits);
		writeLong(buffer, 8, leastSigBits);
		return buffer;
	}


	static void writeLong(byte[] buffer, int byteOffset, long value) {
		buffer[0 + byteOffset] = (byte) (value >>> 56);
		buffer[1 + byteOffset] = (byte) (value >>> 48);
		buffer[2 + byteOffset] = (byte) (value >>> 40);
		buffer[3 + byteOffset] = (byte) (value >>> 32);
		buffer[4 + byteOffset] = (byte) (value >>> 24);
		buffer[5 + byteOffset] = (byte) (value >>> 16);
		buffer[6 + byteOffset] = (byte) (value >>> 8);
		buffer[7 + byteOffset] = (byte) (value);
	}

}
