package net.javaguides.springboot.repository;

import net.javaguides.springboot.entity.Order;
import net.javaguides.springboot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
