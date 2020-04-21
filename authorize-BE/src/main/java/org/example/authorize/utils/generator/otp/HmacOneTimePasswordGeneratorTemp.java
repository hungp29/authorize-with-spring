package org.example.authorize.utils.generator.otp;

import javax.crypto.Mac;
import javax.crypto.ShortBufferException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class HmacOneTimePasswordGeneratorTemp {

    private final Mac mac;
    private final int passwordLength;

    private final byte[] buffer;
    private final int modDivisor;

    /**
     * The default length, in decimal digits, for one-time passwords.
     */
    public static final int DEFAULT_PASSWORD_LENGTH = 6;

    /**
     * The HMAC algorithm specified by the HOTP standard.
     */
    public static final String HOTP_HMAC_ALGORITHM = "HmacSHA1";

    public HmacOneTimePasswordGeneratorTemp() throws NoSuchAlgorithmException {
        this(DEFAULT_PASSWORD_LENGTH);
    }

    public HmacOneTimePasswordGeneratorTemp(final int passwordLength) throws NoSuchAlgorithmException {
        this(passwordLength, HOTP_HMAC_ALGORITHM);
    }

    protected HmacOneTimePasswordGeneratorTemp(final int passwordLength, final String algorithm) throws NoSuchAlgorithmException {
        this.mac = Mac.getInstance(algorithm);

        switch (passwordLength) {
            case 6: {
                this.modDivisor = 1_000_000;
                break;
            }

            case 7: {
                this.modDivisor = 10_000_000;
                break;
            }

            case 8: {
                this.modDivisor = 100_000_000;
                break;
            }

            default: {
                throw new IllegalArgumentException("Password length must be between 6 and 8 digits.");
            }
        }

        this.passwordLength = passwordLength;
        this.buffer = new byte[this.mac.getMacLength()];
    }

    public synchronized int generateOneTimePassword(final Key key, final long counter) throws InvalidKeyException {
        this.mac.init(key);

        this.buffer[0] = (byte) ((counter & 0xff00000000000000L) >>> 56);
        this.buffer[1] = (byte) ((counter & 0x00ff000000000000L) >>> 48);
        this.buffer[2] = (byte) ((counter & 0x0000ff0000000000L) >>> 40);
        this.buffer[3] = (byte) ((counter & 0x000000ff00000000L) >>> 32);
        this.buffer[4] = (byte) ((counter & 0x00000000ff000000L) >>> 24);
        this.buffer[5] = (byte) ((counter & 0x0000000000ff0000L) >>> 16);
        this.buffer[6] = (byte) ((counter & 0x000000000000ff00L) >>> 8);
        this.buffer[7] = (byte) (counter & 0x00000000000000ffL);

        this.mac.update(this.buffer, 0, 8);

        try {
            this.mac.doFinal(this.buffer, 0);
        } catch (final ShortBufferException e) {
            // We allocated the buffer to (at least) match the size of the MAC length at construction time, so this
            // should never happen.
            throw new RuntimeException(e);
        }

        final int offset = this.buffer[this.buffer.length - 1] & 0x0f;

        return ((this.buffer[offset] & 0x7f) << 24 |
                (this.buffer[offset + 1] & 0xff) << 16 |
                (this.buffer[offset + 2] & 0xff) << 8 |
                (this.buffer[offset + 3] & 0xff)) %
                this.modDivisor;
    }

    /**
     * Returns the length, in decimal digits, of passwords produced by this generator.
     *
     * @return the length, in decimal digits, of passwords produced by this generator
     */
    public int getPasswordLength() {
        return this.passwordLength;
    }

    /**
     * Returns the name of the HMAC algorithm used by this generator.
     *
     * @return the name of the HMAC algorithm used by this generator
     */
    public String getAlgorithm() {
        return this.mac.getAlgorithm();
    }
}
