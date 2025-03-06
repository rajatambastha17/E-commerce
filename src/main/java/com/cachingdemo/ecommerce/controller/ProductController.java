package com.cachingdemo.ecommerce.controller;

import com.cachingdemo.ecommerce.model.Product;
import com.cachingdemo.ecommerce.service.ProductService;
import com.cachingdemo.ecommerce.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(Constants.GET_ALL_PRODUCTS)
    public ResponseEntity<List<Product>> getAllProducts() {
        long start = System.currentTimeMillis();
        ResponseEntity<List<Product>> response = ResponseEntity.ok(productService.getAllProducts());
        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start));
        return response;
    }

    @GetMapping(Constants.GET_PRODUCT_BY_ID)
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(Constants.CREATE_PRODUCT)
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productService.evictAllProductsCache();
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PostMapping(Constants.BULK_INSERT_PRODUCT)
    public ResponseEntity<List<Product>> createProducts(@RequestBody List<Product> products) {
        return ResponseEntity.ok(productService.createProducts(products));
    }

    @PutMapping(Constants.UPDATE_PRODUCT)
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping(Constants.DELETE_PRODUCT)
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        Optional<Product> deletedProduct = productService.deleteProduct(id);
        return deletedProduct.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(Constants.EVICT_CACHE)
    public void evictCache() {
        productService.evictAllProductsCache();
    }
}
