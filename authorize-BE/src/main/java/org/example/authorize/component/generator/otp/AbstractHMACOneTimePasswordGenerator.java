package org.example.authorize.component.generator.otp;

import org.example.authorize.exception.AlgorithmException;
import org.example.authorize.exception.OTPException;
import org.example.authorize.utils.constants.Constants;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractHMACOneTimePasswordGenerator<T> implements OTPGenerator<T> {

    // These are used to calculate the check-sum digits.
    //                                          0  1  2  3  4  5  6  7  8  9
    private static final int[] DOUBLE_DIGITS = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

    //                                         0  1   2    3     4      5       6        7         8
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};


    /**
     * Calculates the checksum using the credit card algorithm.
     * This algorithm has the advantage that it detects any single mistyped digit and any single transposition of
     * adjacent digits.
     *
     * @param num    the number to calculate the checksum for
     * @param digits number of significant places in the number
     * @return the checksum of num
     */
    protected int calcChecksum(long num, int digits) {
        boolean doubleDigit = true;
        int total = 0;
        while (0 < digits--) {
            int digit = (int) (num % 10);
            num /= 10;
            if (doubleDigit) {
                digit = DOUBLE_DIGITS[digit];
            }
            total += digit;
            doubleDigit = !doubleDigit;
        }
        int result = total % 10;
        if (result > 0) {
            result = 10 - result;
        }
        return result;
    }

    /**
     * This method uses the JCE to provide the HMAC-SHA-1 algorithm.
     * HMAC computes a Hashed Message Authentication Code and in this case SHA1 is the hash algorithm used.
     *
     * @param keyBytes the bytes to use for the HMAC-SHA-1 key
     * @param text     the message or text to be authenticated.
     */
    protected byte[] hmacHash(byte[] keyBytes, byte[] text) {
        try {
            Mac mac = Mac.getInstance(getAlgorithm());

            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            mac.init(macKey);
            return mac.doFinal(text);
        } catch (NoSuchAlgorithmException e) {
            throw new AlgorithmException("Cannot create " + getAlgorithm(), e);
        } catch (InvalidKeyException e) {
            throw new OTPException("Invalid " + getAlgorithm() + " key", e);
        }
    }

    /**
     * This method generates an OTP value for the given set of parameters, with value of moving factor is Long.
     *
     * @param secret           the shared secret
     * @param movingFactor     the counter, time, or other value that changes on a per use basis.
     * @param codeDigits       the number of digits in the OTP, not including the checksum, if any.
     * @param addChecksum      a flag that indicates if a checksum digit should be appended to the OTP.
     * @param truncationOffset the offset into the MAC result to begin truncation.  If this value is out of
     *                         the range of 0 ... 15, then dynamic truncation  will be used.
     *                         Dynamic truncation is when the last 4 bits of the last byte of the MAC are
     *                         used to determine the start offset.
     * @return A numeric String in base 10 that includes digits plus the optional checksum digit if requested.
     */
    protected String generateOTPByHMACAlgorithm(byte[] secret, long movingFactor, int codeDigits, boolean addChecksum, int truncationOffset) {
        // Put movingFactor value into text byte array
        int digits = addChecksum ? (codeDigits + 1) : codeDigits;
        byte[] text = new byte[8];
        for (int i = text.length - 1; i >= 0; i--) {
            text[i] = (byte) (movingFactor & 0xff);
            movingFactor >>= 8;
        }

        // Compute hmac hash
        byte[] hash = hmacHash(secret, text);

        // Put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;
        if ((0 <= truncationOffset) && (truncationOffset < (hash.length - 4))) {
            offset = truncationOffset;
        }
        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8)
                | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];
        if (addChecksum) {
            otp = (otp * 10) + calcChecksum(otp, codeDigits);
        }
        StringBuilder result = new StringBuilder(String.valueOf(otp));
        while (result.length() < digits) {
            result.insert(0, Constants.ZERO);
        }
        return result.toString();
    }

    /**
     * This method generates an OTP value for the given set of parameters.
     *
     * @param secret           the shared secret
     * @param movingFactor     the counter, time, or other value that changes on a per use basis.
     * @param codeDigits       the number of digits in the OTP, not including the checksum, if any.
     * @param addChecksum      a flag that indicates if a checksum digit should be appended to the OTP.
     * @param truncationOffset the offset into the MAC result to begin truncation.  If this value is out of
     *                         the range of 0 ... 15, then dynamic truncation  will be used.
     *                         Dynamic truncation is when the last 4 bits of the last byte of the MAC are
     *                         used to determine the start offset.
     * @return A numeric String in base 10 that includes digits plus the optional checksum digit if requested.
     */
    public abstract String generate(byte[] secret, T movingFactor, int codeDigits, boolean addChecksum, int truncationOffset);

    /**
     * Get algorithm.
     *
     * @return return algorithm
     */
    protected abstract String getAlgorithm();
}
