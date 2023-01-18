package com.example.onlinestore.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // todo: BadRequest instead 500
    @PostMapping()
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        return new ResponseEntity<>(productService.addProduct(productCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
            @RequestBody ProductUpdateDTO updateProduct) {
        return new ResponseEntity<>(productService.updateProductById(productId, updateProduct), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

}
