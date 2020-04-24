package org.example.authorize.utils.constants;

/**
 * URL Constants.
 */
public class URLConstants {

    /**
     * Prevents new instance.
     */
    private URLConstants() {
    }

    public static final String C_AUTHENTICATION = "/api/v1/authentications";
    public static final String M_AUTHENTICATION = "";
    public static final String M_REFRESH_TOKEN = "/refresh";
    public static final String M_OTP = "/otp";

    public static final String C_ACCOUNT = "/api/v1/accounts";
    public static final String M_ADD_PHONE = "/{id}/phone";

}