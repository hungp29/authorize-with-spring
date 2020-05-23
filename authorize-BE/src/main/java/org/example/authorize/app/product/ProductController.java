package org.example.authorize.app.product;

import lombok.RequiredArgsConstructor;
import org.example.authorize.component.httpdefault.DefaultHttpRestController;
import org.example.authorize.component.version.APIVersion;
import org.example.authorize.entity.Product;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.utils.constants.PermissionGroupConstants;
import org.example.authorize.utils.constants.URLConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Product Controller.
 */
@APIVersion("1.0")
@RestController
@PermissionGroup(PermissionGroupConstants.PRODUCT)
@RequestMapping(URLConstants.C_PRODUCT)
@RequiredArgsConstructor
public class ProductController extends DefaultHttpRestController<Product> {

    private final ProductService productService;

}
