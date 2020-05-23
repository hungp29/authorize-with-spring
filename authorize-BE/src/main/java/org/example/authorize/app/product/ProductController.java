package org.example.authorize.app.product;

import lombok.RequiredArgsConstructor;
import org.example.authorize.component.httpdefault.DefaultHttpRestController;
import org.example.authorize.entity.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Product Controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController extends DefaultHttpRestController<Product> {

    private final ProductService productService;

}
