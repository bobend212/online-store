package com.example.onlinestore.product;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.onlinestore.exception.NotFoundException;

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
        return products.stream().map((product) -> ProductMapper.MAPPER.productToDto(product)).toList();
    }

    public ProductDTO getSingleProductById(Long productId) {
        return productRepository.findById(productId).map(product -> {
            return productMapper.productToDto(product);
        }).orElseThrow(() -> new NotFoundException(MessageFormat.format("Product with ID: {0} not found.", productId)));
    }

    public ProductDTO addProduct(ProductCreateDTO productCreateDTO) {
        return productMapper.productToDto(productRepository.save(Product.builder()
                .name(productCreateDTO.getName())
                .price(productCreateDTO.getPrice())
                .stockQty(productCreateDTO.getStockQty())
                .inStock(productCreateDTO.getInStock())
                .build()));
    }

    public ProductDTO updateProductById(Long productId, ProductUpdateDTO productUpdate) {
        return productRepository.findById(productId).map(product -> {
            product.setName(productUpdate.getName());
            product.setPrice(productUpdate.getPrice());
            product.setStockQty(productUpdate.getStockQty());
            product.setInStock(productUpdate.getInStock());
            return productMapper.productToDto(productRepository.save(product));
        }).orElseThrow(() -> new NotFoundException(MessageFormat.format("Product with ID: {0} not found.", productId)));
    }

    public Boolean deleteProduct(Long productId) {
        return productRepository.findById(productId).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElseThrow(() -> new NotFoundException(MessageFormat.format("Product with ID: {0} not found.", productId)));
    }
}
