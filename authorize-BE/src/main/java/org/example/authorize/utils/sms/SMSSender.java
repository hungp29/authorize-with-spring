package org.example.authorize.utils.sms;

import java.util.Map;

/**
 * SMS sender.
 */
public interface SMSSender {

    /**
     * Send sms.
     *
     * @param phone    phone number to send sms
     * @param template template of message
     * @param data     data to merge with template
     */
    void sendSMS(String phone, String template, Map<String, String> data);
}
