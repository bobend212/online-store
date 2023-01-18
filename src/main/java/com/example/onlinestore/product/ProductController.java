package com.example.onlinestore.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getSingleProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getSingleProductById(productId), HttpStatus.OK);
    }

    // @PostMapping()
    // public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody
    // ProductCreateDTO productCreateDTO) {
    // return new ResponseEntity<>(productService.addProduct(productCreateDTO),
    // HttpStatus.CREATED);
    // }

}
