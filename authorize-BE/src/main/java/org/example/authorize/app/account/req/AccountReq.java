package org.example.authorize.app.account.req;

import lombok.Builder;
import lombok.Data;

/**
 * Account request.
 */
@Data
@Builder
public class AccountReq {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;
}
