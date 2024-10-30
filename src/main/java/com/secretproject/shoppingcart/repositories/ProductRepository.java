package com.secretproject.shoppingcart.repositories;

import com.secretproject.shoppingcart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

}
