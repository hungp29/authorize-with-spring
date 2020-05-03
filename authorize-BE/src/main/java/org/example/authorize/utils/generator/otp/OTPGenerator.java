package org.example.authorize.utils.generator.otp;

/**
 * This class contains methods that are used to calculate the One-Time Password (OTP).
 *
 * @param <T>
 */
public interface OTPGenerator<T> {

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
    String generate(byte[] secret, T movingFactor, int codeDigits, boolean addChecksum, int truncationOffset);
}
