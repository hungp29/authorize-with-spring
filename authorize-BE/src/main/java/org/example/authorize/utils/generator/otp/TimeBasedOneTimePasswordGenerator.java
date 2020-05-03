package org.example.authorize.utils.generator.otp;

import lombok.RequiredArgsConstructor;
import org.example.authorize.config.prop.OTPProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;

/**
 * This class is extension of {@code AbstractHMACOneTimePasswordGenerator} class.
 * It use time-step as counter.
 * https://tools.ietf.org/html/rfc6238
 */
@Component("TOTP")
@RequiredArgsConstructor
public class TimeBasedOneTimePasswordGenerator extends AbstractHMACOneTimePasswordGenerator<Instant> {

    private static final int DEFAULT_TIME_STEP = 30;

    private final OTPProperties otpProperties;

    @Override
    protected String getAlgorithm() {
        return otpProperties.getAlgorithm();
    }

    @Override
    public String generate(byte[] secret, Instant movingFactor, int codeDigits, boolean addChecksum, int truncationOffset) {
        Assert.notNull(movingFactor, "Moving Factor cannot be null");
        Duration timeStep = Duration.ofSeconds(otpProperties.getTimeStep() > 0 ? otpProperties.getTimeStep() : DEFAULT_TIME_STEP);
        return generateOTPByHMACAlgorithm(secret, movingFactor.toEpochMilli() / timeStep.toMillis(), codeDigits, addChecksum, truncationOffset);
    }
}
