package net.javaguides.springboot.repository;

import net.javaguides.springboot.entity.Product;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findProductsByUsersId(Long userId);

}
