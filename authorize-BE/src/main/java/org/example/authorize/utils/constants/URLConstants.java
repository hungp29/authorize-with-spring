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

    public static final String C_AUTHENTICATION = "/api/authentications";
    public static final String M_AUTHENTICATION = "";
    public static final String M_REFRESH_TOKEN = "/refresh";
    public static final String M_OTP = "/otp";
    public static final String M_OTP_VERIFY = M_OTP + "/verify";

    public static final String C_ACCOUNT = "/api/accounts";
    public static final String M_ADD_PHONE = "/{id}/phone";
    public static final String M_ADD_EMAIL = "/{id}/email";

    public static final String C_PERMISSION = "/api/permissions";
    public static final String M_GET_PERMISSION = "";

    public static final String C_TEST = "/api/test";
    public static final String M_TEST = "";

}