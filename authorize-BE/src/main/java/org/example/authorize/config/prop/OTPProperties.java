package org.example.authorize.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OTP Properties.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "otp", ignoreUnknownFields = false)
public class OTPProperties {

    private String algorithm;

    private int timeStep;

    private int otpLength = 6;

    private int validitySeconds;
}
