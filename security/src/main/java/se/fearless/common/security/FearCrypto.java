package se.fearless.common.security;


public interface FearCrypto {
    byte[] generateKey() throws FearlessCryptoException;

    byte[] generateKey(String seed) throws FearlessCryptoException;

    byte[] encrypt(byte[] key, byte[] clearData) throws FearlessCryptoException;

    byte[] decrypt(byte[] key, byte[] encryptedData) throws FearlessCryptoException;
}
