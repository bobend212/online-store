package com.example.onlinestore.product;

import org.springframework.stereotype.Service;

import com.example.onlinestore.exception.NotFoundException;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map((product) -> ProductMapper.MAPPER.productToDto(product))
                .collect(Collectors.toList());
    }

    public ProductDTO getSingleProductById(Long productId) {
        return productRepository.findById(productId).map(productEntity -> {
            return productMapper.productToDto(productEntity);
        }).orElseThrow(() -> new NotFoundException(MessageFormat.format("Product with ID: {0} not found.", productId)));

        // Product product = productRepository.findById(productId).get();
        // return productMapper.productToDto(product);
    }

    // public ProductDTO addProduct(ProductCreateDTO productCreateDTO) {
    // return ProductDTO.toDto(productRepository.save(Product.builder()
    // .name(productCreateDTO.getName())
    // .price(productCreateDTO.getPrice())
    // .stockQty(productCreateDTO.getStockQty())
    // .inStock(productCreateDTO.getInStock())
    // .build()));
    // }

}
