package org.ecommerce.ecommerce.controller;


import org.ecommerce.ecommerce.entity.Product;
import org.ecommerce.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(
            @RequestParam(name = "searchQuery", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
            {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Search query must not be blank or empty.");
        }


        Pageable pageable = PageRequest.of(page, size);
        Page<Product> results = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(results);
            }
}
