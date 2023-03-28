package net.javaguides.springboot.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "order_product",
            joinColumns = { @JoinColumn(name = "orders_id") },
            inverseJoinColumns = { @JoinColumn(name = "products_id") })
    private Set<Product> products = new HashSet<>();

    public Order() {
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
