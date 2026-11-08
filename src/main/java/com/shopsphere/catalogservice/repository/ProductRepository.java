package com.shopsphere.catalogservice.repository;

import com.shopsphere.catalogservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);
}
