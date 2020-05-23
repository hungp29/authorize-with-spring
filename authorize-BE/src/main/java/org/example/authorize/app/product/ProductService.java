package org.example.authorize.app.product;

import lombok.RequiredArgsConstructor;
import org.example.authorize.component.httpdefault.DefaultHttpService;
import org.example.authorize.entity.Product;
import org.springframework.stereotype.Service;

/**
 * Product service.
 */
@Service
@RequiredArgsConstructor
public class ProductService extends DefaultHttpService<Product> {

    private final ProductRepository productRepository;
}
