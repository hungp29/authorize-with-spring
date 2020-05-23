package org.example.authorize.component.httpdefault;

/**
 * Special Value.
 */
public enum SpecialValue {

    NONE(""),
    CURRENT_USER_ID("CURRENT_USER_ID");

    private String code;

    SpecialValue(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
