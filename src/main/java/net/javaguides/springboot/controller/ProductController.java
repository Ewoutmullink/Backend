package net.javaguides.springboot.controller;

import java.util.List;

import net.javaguides.springboot.entity.Product;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.repository.ProductRepository;
import net.javaguides.springboot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import net.javaguides.springboot.exception.ResourceNotFoundException;

@RestController
@CrossOrigin(origins ="*", exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable (value = "id") long productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id :" + productId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(@RequestBody Product product) {
        return this.productRepository.save(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(@RequestBody Product product, @PathVariable ("id") long productId) {
        Product existingProduct = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + productId));
        existingProduct.setDescription(product.getDescription());
        existingProduct.setName(product.getName());
        existingProduct.setPictureUrl(product.getPictureUrl());
        existingProduct.setPrice(product.getPrice());
        return this.productRepository.save(existingProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> deleteProduct(@PathVariable ("id") long userId){
        Product existingProduct = this.productRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id :" + userId));
        this.productRepository.delete(existingProduct);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/products")
    public ResponseEntity<List<Product>> getAllTagsByTutorialId(@PathVariable(value = "userId") Long userId) {
        if (this.userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found User with id = " + userId);
        }

        List<Product> products = productRepository.findProductsByUsersId(userId);
        products.forEach(product -> {
            System.out.println(product.getId());
            System.out.println("mm");
        });

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/card/{userId}/")
    public ResponseEntity<Product> addTag(@PathVariable(value = "userId") Long userId, @RequestBody Product productRequest) {
        System.out.println("lol");
        Product product = this.userRepository.findById(userId).map(user -> {
            long productId = productRequest.getId();

            if (productId != 0L) {
                Product _tag = this.productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id = " + productId));
                user.addProduct(_tag);
                this.userRepository.save(user);
                return _tag;
            }

            user.addProduct(productRequest);
            return this.productRepository.save(productRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = "));

        return new ResponseEntity<>(productRequest, HttpStatus.CREATED);

    }
    @DeleteMapping ("/{userId}/products/{productId}")
    public ResponseEntity<HttpStatus> deleteProductFromUser(@PathVariable(value = "userId") Long userId, @PathVariable(value = "productId") Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        user.removeProduct(productId);
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
