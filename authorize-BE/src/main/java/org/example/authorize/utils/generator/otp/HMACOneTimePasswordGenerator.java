package org.example.authorize.utils.generator.otp;

import lombok.RequiredArgsConstructor;
import org.example.authorize.config.prop.OtpProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * This class contains methods that are used to calculate the
 * One-Time Password (OTP) using JCE to provide the HMAC-SHA-1.
 * https://tools.ietf.org/html/rfc4226
 */
@Component("HOTP")
@RequiredArgsConstructor
public class HMACOneTimePasswordGenerator extends AbstractHMACOneTimePasswordGenerator<Long> {

    private final OtpProperties otpProperties;

    protected String getAlgorithm() {
        return otpProperties.getAlgorithm();
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
    public String generate(byte[] secret, Long movingFactor, int codeDigits, boolean addChecksum, int truncationOffset) {
        Assert.notNull(movingFactor, "Moving Factor cannot be null");
        return generateOTPByHMACAlgorithm(secret, movingFactor, codeDigits, addChecksum, truncationOffset);
    }
}