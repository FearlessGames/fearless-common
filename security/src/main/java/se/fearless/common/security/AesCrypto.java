package se.fearless.common.security;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AesCrypto implements FearCrypto {
    private final String CIPHER = "AES/CBC/PKCS5Padding";

    @Override
    public byte[] generateKey() throws FearlessCryptoException {
        return generateKey(null);
    }

    @Override
    public byte[] generateKey(String seed) throws FearlessCryptoException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            if (seed != null && seed.isEmpty()) {
                secureRandom.setSeed(seed.getBytes());
            }
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new FearlessCryptoException(e);
        }
    }

    @Override
    public byte[] encrypt(byte[] key, byte[] clearData) throws FearlessCryptoException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(clearData);
        } catch (IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new FearlessCryptoException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] key, byte[] encryptedData) throws FearlessCryptoException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException e) {
            throw new FearlessCryptoException(e);
        }
    }
}
