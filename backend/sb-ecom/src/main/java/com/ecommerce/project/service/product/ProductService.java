package com.ecommerce.project.service.product;


import com.ecommerce.project.payload.product.ProductDTO;
import com.ecommerce.project.payload.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {


    ProductDTO addProduct(ProductDTO product, Long categoryId);


    ProductResponse productsByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String orderBy);

    ProductResponse searchByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String orderBy);

    ProductDTO updateProduct(Long productId, ProductDTO productDto);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile file) throws IOException;

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String orderBy);
}
