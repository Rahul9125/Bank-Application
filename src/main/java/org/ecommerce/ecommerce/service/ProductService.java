package org.ecommerce.ecommerce.service;


import org.ecommerce.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {
//    List<Product> searchProducts(String keyword);
      Page<Product> searchProducts(String keyword, Pageable pageable);

}
