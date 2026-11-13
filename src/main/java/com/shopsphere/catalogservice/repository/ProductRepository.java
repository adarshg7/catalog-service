package com.shopsphere.catalogservice.repository;

import com.shopsphere.catalogservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);
    Optional<Product> findByIdAndActiveTrue(Long id);
    Page<Product> findByActiveTrue(Pageable pageable);
}
