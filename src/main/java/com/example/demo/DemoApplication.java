package com.example.demo;

import com.example.demo.entities.InvLine;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@RestController
@RequestMapping("/product")
class ProductApi {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private invLineRepository invLineRepository;

    @GetMapping("/all")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productQuantity")
    public List invLineJoin() {
        return invLineRepository.invLineJoin();
    }

}

@RepositoryRestResource(path = "product")
interface ProductRepository extends JpaRepository<Product, Integer> {
}

@RepositoryRestResource(path = "invLine")
interface invLineRepository extends JpaRepository<InvLine, Integer> {
     @Query("SELECT new com.example.demo.entities.ProductQuantity(p.id, sum(i.qty)) FROM InvLine i JOIN i.product p GROUP BY p")
     List<ProductQuantity> invLineJoin();
}

