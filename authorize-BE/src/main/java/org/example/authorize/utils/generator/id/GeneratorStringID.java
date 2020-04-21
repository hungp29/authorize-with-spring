package org.example.authorize.utils.generator.id;

import org.example.authorize.exception.GeneratorException;
import org.example.authorize.utils.constants.Constants;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Generator String ID class.
 */
@Component
public class GeneratorStringID implements Generator<String> {

    private static final char[] hexArray = Constants.HEX_VALUE.toCharArray();

    /**
     * Generating ID.
     *
     * @return return the ID is generated
     */
    @Override
    public String generate() {
        try {
            MessageDigest salt = MessageDigest.getInstance("SHA-256");
            salt.update(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
            return bytesToHex(salt.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new GeneratorException("An error occurs while generating ID");
        }
    }

    /**
     * Generating ID with value is determined.
     *
     * @param value the value to encrypt to id
     * @return return the ID after encrypt value
     */
    @Override
    public String generate(String value) {
        try {
            MessageDigest salt = MessageDigest.getInstance("SHA-256");
            salt.update(value.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(salt.digest());
        } catch (NoSuchAlgorithmException | NullPointerException e) {
            throw new GeneratorException("An error occurs while generating ID");
        }
    }

    /**
     * Convert bytes to hex string.
     *
     * @param bytes the value need to convert
     * @return return the hex value after converting
     */
    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF; // 0xFF is 255, in bit is 00000000000000000000000011111111
            hexChars[i] = hexArray[v >>> 4]; // Max value of v >>> 4 is 00001111 ('15' in decimal, 'F' in hexadecimal)
        }
        return new String(hexChars);
    }
}
