package com.cachingdemo.ecommerce.service;

import com.cachingdemo.ecommerce.model.Product;
import com.cachingdemo.ecommerce.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Cacheable("products")
    public List<Product> getAllProducts() {
        log.info("Fetching all products from the database.");
        
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        log.info("Fetching product with ID " + id + " from the database.");
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "products::#id"}, allEntries = false)
    public Optional<Product> deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            productRepository.deleteById(id);
            return productOptional;
        }
        return Optional.empty();
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public List<Product> createProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void evictAllProductsCache() {
    }
}
