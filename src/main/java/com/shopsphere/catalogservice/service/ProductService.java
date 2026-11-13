package com.shopsphere.catalogservice.service;

import com.shopsphere.catalogservice.config.RedisConfig;
import com.shopsphere.catalogservice.dto.ProductRequest;
import com.shopsphere.catalogservice.dto.ProductResponse;
import com.shopsphere.catalogservice.entity.Product;
import com.shopsphere.catalogservice.exception.ProductNotFoundException;
import com.shopsphere.catalogservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable(value = RedisConfig.PRODUCT_CACHE ,key = "#id")
    public ProductResponse getProduct(Long id){
        log.info("Cache MISS - loading product {} from DB", id);
        Product product = productRepository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
            
        return ProductResponse.from(product);
    }

    @Cacheable(value = RedisConfig.PRODUCT_LIST_CACHE, key = "#category + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponse> listProducts(String category, Pageable pageable){
        log.info("Cache MISS - loading product list (category = {}) from db", category);

        Page<Product> page = (category == null || category.isBlank()) ? productRepository.findByActiveTrue(pageable) : productRepository.findByCategoryAndActiveTrue(category,pageable);

        return page.map(ProductResponse::from);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request){
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .active(true)
                .build();

        Product saved = productRepository.save(product);

        log.info("Created product id = {}", saved.getId());

        return ProductResponse.from(saved);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value =  RedisConfig.PRODUCT_CACHE, key = "#id"),
            @CacheEvict(value = RedisConfig.PRODUCT_LIST_CACHE, allEntries = true)
    })
    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());

        Product saved = productRepository.save(product);
        log.info("Updated product id={}",id);
        return ProductResponse.from(saved);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = RedisConfig.PRODUCT_CACHE, key = "#id"),
            @CacheEvict(value = RedisConfig.PRODUCT_LIST_CACHE,allEntries = true)
    })
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setActive(false);
        productRepository.save(product);
        log.info("Soft-deleted product id={}", id);
    }
}
