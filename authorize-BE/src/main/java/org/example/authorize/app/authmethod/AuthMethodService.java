package org.example.authorize.app.authmethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authorize.config.prop.OTPProperties;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.entity.AuthMethodData;
import org.example.authorize.enums.AuthType;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.exception.UsernameAlreadyExistException;
import org.example.authorize.utils.PasswordEncode;
import org.example.authorize.utils.SecurityUtils;
import org.example.authorize.utils.generator.id.Generator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final AuthMethodDataRepository authMethodDataRepository;

    /**
     * Create auth method USERNAME and PASSWORD.
     *
     * @param username username
     * @param password password
     * @return return auth method
     */
    public AuthMethod createAuthMethodUsername(String username, String password) {
        AuthMethodData authMethodData = new AuthMethodData();
        authMethodData.setId(generator.generate());
        authMethodData.setAuthData1(passwordEncode.encode(password));

        return createAuthMethodUsername(username, authMethodData);
    }

    /**
     * Create auth method USERNAME and PASSWORD.
     *
     * @param username       username
     * @param authMethodData auth method data
     * @return return auth method
     */
    public AuthMethod createAuthMethodUsername(String username, AuthMethodData authMethodData) {
        Assert.notNull(authMethodData, "AuthMethodData cannot be null");
        if (checkAuthMethodUsernameExist(username)) {
            throw new UsernameAlreadyExistException("Username " + username + " is exist");
        }
        AuthMethod authMethod = new AuthMethod();
        authMethod.setId(generator.generate());
        authMethod.setAuthType(AuthType.USERNAME_PASSWORD);
        authMethod.setDetermineId(username);
        authMethod.setAuthMethodData(authMethodData);

        return authMethod;
    }

    /**
     * Create Auth method email.
     *
     * @param email    the email
     * @param password the password
     * @return return auth method
     */
    public AuthMethod createAuthMethodEmail(String email, String password) {
        AuthMethodData authMethodData = new AuthMethodData();
        authMethodData.setId(generator.generate());
        authMethodData.setAuthData1(passwordEncode.encode(password));

        return createAuthMethodEmail(email, authMethodData);
    }

    /**
     * Create Auth method email.
     *
     * @param email          the email
     * @param authMethodData the auth method data
     * @return return auth method
     */
    public AuthMethod createAuthMethodEmail(String email, AuthMethodData authMethodData) {
        Assert.notNull(authMethodData, "AuthMethodData cannot be null");
        if (checkAuthMethodEmailExist(email)) {
            throw new UsernameAlreadyExistException("Email " + email + " is exist");
        }
        AuthMethod authMethod = new AuthMethod();
        authMethod.setId(generator.generate());
        authMethod.setAuthType(AuthType.EMAIL_PASSWORD);
        authMethod.setDetermineId(email);

        authMethod.setAuthMethodData(authMethodData);
        return authMethod;
    }

    /**
     * Create auth method PHONE NUMBER.
     *
     * @param phoneNumber phone number
     * @return return auth method
     */
    public AuthMethod createAuthMethodPhoneNumber(String phoneNumber) {
        if (checkAuthMethodPhoneExist(phoneNumber)) {
            throw new UsernameAlreadyExistException("Phone number " + phoneNumber + " is exist");
        }
        AuthMethod authMethod = new AuthMethod();
        authMethod.setId(generator.generate());
        authMethod.setAuthType(AuthType.PHONE_NUMBER);
        authMethod.setDetermineId(phoneNumber);

        AuthMethodData authMethodData = new AuthMethodData();
        authMethodData.setId(generator.generate());
        authMethodData.setAuthData1(SecurityUtils.generateSecretKey());

        authMethod.setAuthMethodData(authMethodData);
        return authMethod;
    }

    /**
     * Re-generate secret key for phone auth method.
     *
     * @param authMethod authentication method for phone number
     * @return true true if re-generate and save successfully, otherwise return false
     */
    public AuthMethod regenerateSecretKeyForPhoneAuthMethod(AuthMethod authMethod) {
        if (null != authMethod && AuthType.PHONE_NUMBER.equals(authMethod.getAuthType()) &&
                null != authMethod.getAuthMethodData()) {
            authMethod.getAuthMethodData().setAuthData1(SecurityUtils.generateSecretKey());
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
        if (null != authMethod && AuthType.PHONE_NUMBER.equals(authMethod.getAuthType()) &&
                null != authMethod.getAuthMethodData()) {
            movingFactor = getMovingFactorOfPhoneAuthMethod(authMethod);
            authMethod.getAuthMethodData().setAuthData2(String.valueOf(++movingFactor));
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
        if (null != authMethod && AuthType.PHONE_NUMBER.equals(authMethod.getAuthType()) &&
                null != authMethod.getAuthMethodData()) {
            try {
                movingFactor = Integer.parseInt(authMethod.getAuthMethodData().getAuthData2());
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
        if (null != authMethod && AuthType.PHONE_NUMBER.equals(authMethod.getAuthType()) &&
                null != authMethod.getAuthMethodData()) {
            authMethod.getAuthMethodData().setExpireDate(LocalDateTime.now().plusSeconds(otpProps.getValiditySeconds()));
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
        return authMethodRepository.findByAuthTypeAndDetermineId(AuthType.USERNAME_PASSWORD, username).isPresent();
    }

    /**
     * Check Email is exist or not.
     *
     * @param email email to check
     * @return return true if email is exist, otherwise return false
     */
    public boolean checkAuthMethodEmailExist(String email) {
        return authMethodRepository.findByAuthTypeAndDetermineId(AuthType.EMAIL_PASSWORD, email).isPresent();
    }

    /**
     * Check Phone is exist or not.
     *
     * @param phone phone number to check
     * @return return true if phone number is exist, otherwise return false
     */
    public boolean checkAuthMethodPhoneExist(String phone) {
        return authMethodRepository.findByAuthTypeAndDetermineId(AuthType.EMAIL_PASSWORD, phone).isPresent();
    }

    @Transactional
    public AuthMethod save(AuthMethod authMethod) {
        if (null != authMethod && null != authMethod.getAuthMethodData()) {
            AuthMethodData authMethodData = authMethodDataRepository.save(authMethod.getAuthMethodData());
            authMethod.setAuthMethodData(authMethodData);
            return authMethodRepository.save(authMethod);
        }
        throw new SaveEntityException("Auth method is empty, cannot save it");
    }

//    public List<AuthMethod> saveAll(List<AuthMethod> authMethods) {
//        if (!CollectionUtils.isEmpty(authMethods)) {
//
//        }
//    }

    /**
     * Find Auth method by determine.
     *
     * @param determineId determine value
     * @return return auth method if it exist
     */
    public AuthMethod findByDetermineId(String determineId) {
        return authMethodRepository.findByDetermineId(determineId).orElse(null);
    }

    /**
     * Find Auth method by auth type and determine id.
     *
     * @param authType    authentication type
     * @param determineId determine id
     * @return return auth method if it exist
     */
    public AuthMethod findByAuthTypeAndDetermineId(AuthType authType, String determineId) {
        return authMethodRepository.findByAuthTypeAndDetermineId(authType, determineId).orElse(null);
    }

    /**
     * Find Auth method by determine id and list auth type.
     *
     * @param determineId determine id (username, email, phone number)
     * @param authTypes   auth type
     * @return return auth method if it exist
     */
    public AuthMethod findByDetermineIdAndAuthTypes(String determineId, AuthType... authTypes) {
        return authMethodRepository.findByDetermineIdAndAuthTypeIn(determineId, authTypes).orElse(null);
    }

    /**
     * Find Auth Method from list.
     *
     * @param authMethods list auth method
     * @param authType    auth type
     * @return return auth method
     */
    public Optional<AuthMethod> findAuthMethod(List<AuthMethod> authMethods, AuthType authType) {
        if (!CollectionUtils.isEmpty(authMethods) && null != authType) {
            return authMethods.stream()
                    .filter(authMethod -> AuthType.EMAIL_PASSWORD.equals(authMethod.getAuthType()))
                    .findFirst();
        }
        return Optional.empty();
    }
}
