package org.example.authorize.app.account;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.account.req.EmailReq;
import org.example.authorize.app.account.req.PhoneReq;
import org.example.authorize.response.WResponseEntity;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.utils.constants.PermissionGroupConstants;
import org.example.authorize.utils.constants.URLConstants;
import org.springframework.web.bind.annotation.*;

/**
 * Account Controller.
 */
@RestController
@PermissionGroup(PermissionGroupConstants.ACCOUNT)
@RequestMapping(URLConstants.C_ACCOUNT)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Update phone number for account.
     *
     * @param id       id of account
     * @param phoneReq phone number request object
     * @return return true if update successfully
     */
    @PostMapping(URLConstants.M_ADD_PHONE)
    public WResponseEntity<Boolean> addOrUpdatePhoneNumber(@PathVariable String id, @RequestBody PhoneReq phoneReq) {
        return WResponseEntity.success(accountService.addOrUpdatePhoneNumber(id, phoneReq));
    }

    /**
     * Update email for account.
     *
     * @param id       id of account
     * @param emailReq email request object
     * @return return true if update successfully
     */
    @PostMapping(URLConstants.M_ADD_EMAIL)
    public WResponseEntity<Boolean> addOrUpdateEmail(@PathVariable String id, @RequestBody EmailReq emailReq) {
        return WResponseEntity.success(accountService.addOrUpdateEmail(id, emailReq));
    }
}
