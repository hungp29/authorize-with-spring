package org.example.authorize.utils.generator.otp;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

public class TimeBasedOneTimePasswordGeneratorTemp extends HmacOneTimePasswordGeneratorTemp {

    private final Duration timeStep;

    /**
     * The default time-step for a time-based one-time password generator (30 seconds).
     */
    public static final Duration DEFAULT_TIME_STEP = Duration.ofSeconds(30);

    /**
     * A string identifier for the HMAC-SHA1 algorithm (required by HOTP and allowed by TOTP). HMAC-SHA1 is the default
     * algorithm for TOTP.
     */
    public static final String TOTP_ALGORITHM_HMAC_SHA1 = "HmacSHA1";

    /**
     * A string identifier for the HMAC-SHA256 algorithm (allowed by TOTP).
     */
    public static final String TOTP_ALGORITHM_HMAC_SHA256 = "HmacSHA256";

    /**
     * A string identifier for the HMAC-SHA512 algorithm (allowed by TOTP).
     */
    public static final String TOTP_ALGORITHM_HMAC_SHA512 = "HmacSHA512";

    public TimeBasedOneTimePasswordGeneratorTemp() throws NoSuchAlgorithmException {
        this(DEFAULT_TIME_STEP);
    }

    public TimeBasedOneTimePasswordGeneratorTemp(final Duration timeStep) throws NoSuchAlgorithmException {
        this(timeStep, HmacOneTimePasswordGeneratorTemp.DEFAULT_PASSWORD_LENGTH);
    }

    public TimeBasedOneTimePasswordGeneratorTemp(final Duration timeStep, final int passwordLength) throws NoSuchAlgorithmException {
        this(timeStep, passwordLength, TOTP_ALGORITHM_HMAC_SHA1);
    }

    public TimeBasedOneTimePasswordGeneratorTemp(final Duration timeStep, final int passwordLength, final String algorithm) throws NoSuchAlgorithmException {
        super(passwordLength, algorithm);

        this.timeStep = timeStep;
    }

    public int generateOneTimePassword(final Key key, final Instant timestamp) throws InvalidKeyException {
        return this.generateOneTimePassword(key, timestamp.toEpochMilli() / this.timeStep.toMillis());
    }

    public Duration getTimeStep() {
        return this.timeStep;
    }
}
