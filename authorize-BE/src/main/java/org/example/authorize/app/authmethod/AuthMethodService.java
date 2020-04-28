package org.example.authorize.app.authmethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authorize.config.prop.OTPProperties;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.enums.AuthType;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.exception.UsernameAlreadyExistException;
import org.example.authorize.utils.PasswordEncode;
import org.example.authorize.utils.SecurityUtils;
import org.example.authorize.utils.generator.id.Generator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Auth method service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthMethodService {

    private final OTPProperties otpProps;

    private final Generator<String> generator;
    private final PasswordEncode passwordEncode;
    private final AuthMethodRepository authMethodRepository;

    /**
     * Create auth method USERNAME and PASSWORD.
     *
     * @param username username
     * @param password password
     * @return return auth method
     */
    public AuthMethod createAuthMethodByUsernameAndPassword(String username, String password) {
        if (checkAuthMethodUsernameExist(username)) {
            throw new UsernameAlreadyExistException("Username " + username + " is exist");
        }
        AuthMethod authMethod = new AuthMethod();
        authMethod.setId(generator.generate());
        authMethod.setAuthType(AuthType.USERNAME_PASSWORD);
        authMethod.setAuthData1(username);
        authMethod.setAuthData2(passwordEncode.encode(password));
        return authMethod;
    }

    /**
     * Create auth method PHONE NUMBER.
     *
     * @param phoneNumber phone number
     * @return return auth method
     */
    public AuthMethod createAuthMethodByPhoneNumber(String phoneNumber) {
        if (checkAuthMethodPhoneExist(phoneNumber)) {
            throw new UsernameAlreadyExistException("Phone number " + phoneNumber + " is exist");
        }
        AuthMethod authMethod = new AuthMethod();
        authMethod.setId(generator.generate());
        authMethod.setAuthType(AuthType.PHONE_NUMBER);
        authMethod.setAuthData1(phoneNumber);
        authMethod.setAuthData2(SecurityUtils.generateSecretKey());
        return authMethod;
    }

    /**
     * Re-generate secret key for phone auth method.
     *
     * @param authMethod authentication method for phone number
     * @return true true if re-generate and save successfully, otherwise return false
     */
    public AuthMethod regenerateSecretKeyForPhoneAuthMethod(AuthMethod authMethod) {
        if (null != authMethod && AuthType.PHONE_NUMBER.equals(authMethod.getAuthType())) {
            authMethod.setAuthData2(SecurityUtils.generateSecretKey());
            authMethod = save(authMethod);
        }
        return authMethod;
    }

    /**
     * Increase moving factor for authentication method phone.
     *
     * @param authMethod phone authentication method
     * @return return true if update moving factor successfully, otherwise return false
     */
    public long increaseMovingFactorForPhoneAuthMethod(AuthMethod authMethod) {
        long movingFactor = 0;
        if (null != authMethod && AuthType.PHONE_NUMBER.equals(authMethod.getAuthType())) {
            movingFactor = getMovingFactorOfPhoneAuthMethod(authMethod);
            authMethod.setAuthData3(String.valueOf(++movingFactor));
            save(authMethod);
        }
        return movingFactor;
    }

    /**
     * Get moving factor value of phone authentication method.
     *
     * @param authMethod phone authentication method
     * @return return moving factor value
     */
    public long getMovingFactorOfPhoneAuthMethod(AuthMethod authMethod) {
        int movingFactor = 0;
        if (null != authMethod && AuthType.PHONE_NUMBER.equals(authMethod.getAuthType())) {
            try {
                movingFactor = Integer.parseInt(authMethod.getAuthData3());
            } catch (NumberFormatException e) {
                log.warn("Cannot convert moving factor of phone auth method, set moving factor to zero");
            }
        }
        return movingFactor;
    }

    /**
     * Set Expiration for phone number auth method.
     *
     * @param authMethod the phone auth method
     * @return return auth method after set expiration time
     */
    public AuthMethod setExpirationForPhoneAuthMethod(AuthMethod authMethod) {
        if (null != authMethod && AuthType.PHONE_NUMBER.equals(authMethod.getAuthType())) {
            authMethod.setExpireDate(LocalDateTime.now().plusSeconds(otpProps.getValiditySeconds()));
            return save(authMethod);
        }
        return authMethod;
    }

    /**
     * Check Username is exist or not.
     *
     * @param username username to check
     * @return return true if username is exist, otherwise return false
     */
    public boolean checkAuthMethodUsernameExist(String username) {
        return authMethodRepository.findByAuthTypeAndAuthData1(AuthType.USERNAME_PASSWORD, username).isPresent();
    }

    /**
     * Check Email is exist or not.
     *
     * @param email email to check
     * @return return true if email is exist, otherwise return false
     */
    public boolean checkAuthMethodEmailExist(String email) {
        return authMethodRepository.findByAuthTypeAndAuthData1(AuthType.EMAIL_PASSWORD, email).isPresent();
    }

    /**
     * Check Phone is exist or not.
     *
     * @param phone phone number to check
     * @return return true if phone number is exist, otherwise return false
     */
    public boolean checkAuthMethodPhoneExist(String phone) {
        return authMethodRepository.findByAuthTypeAndAuthData1(AuthType.EMAIL_PASSWORD, phone).isPresent();
    }

    @Transactional
    public AuthMethod save(AuthMethod authMethod) {
        if (null != authMethod) {
            return authMethodRepository.save(authMethod);
        }
        throw new SaveEntityException("Auth method is empty, cannot save it");
    }

    /**
     * Find Auth method by auth data 1.
     *
     * @param authData1 auth data 1 value
     * @return return auth method if it exist
     */
    public AuthMethod findByAuthData1(String authData1) {
        return authMethodRepository.findByAuthData1(authData1).orElse(null);
    }

    /**
     * Find Auth method by auth type and auth data 1.
     *
     * @param authType  authentication type
     * @param authData1 authentication data 1
     * @return return auth method if it exist
     */
    public AuthMethod findByAuthTypeAndAuthData1(AuthType authType, String authData1) {
        return authMethodRepository.findByAuthTypeAndAuthData1(authType, authData1).orElse(null);
    }
}
