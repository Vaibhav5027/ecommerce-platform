package com.ecommerce.project.controller;

import com.ecommerce.project.config.ProductApiConstants;
import com.ecommerce.project.payload.product.ProductDTO;
import com.ecommerce.project.payload.product.ProductResponse;
import com.ecommerce.project.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO product, @PathVariable Long categoryId){
        ProductDTO productDTO = productService.addProduct(product, categoryId);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber" ,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize" ,required = false, defaultValue = ProductApiConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy" ,required = false,defaultValue = ProductApiConstants.SORT_BY) String sortBy,
            @RequestParam(name = "orderBy" ,required = false, defaultValue = ProductApiConstants.ORDER_BY) String orderBy
    ){
        ProductResponse allProducts = productService.getAllProducts(pageNumber,pageSize,sortBy,orderBy);
        return ResponseEntity.ok().body(allProducts);
    }
    @GetMapping("/public/categories/{categoryId}/products")
    ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                          @RequestParam(name = "pageNumber" ,required = false) Integer pageNumber,
                                                          @RequestParam(name = "pageSize" ,required = false, defaultValue = ProductApiConstants.PAGE_SIZE) Integer pageSize,
                                                          @RequestParam(name = "sortBy" ,required = false,defaultValue = ProductApiConstants.SORT_BY) String sortBy,
                                                          @RequestParam(name = "orderBy" ,required = false, defaultValue = ProductApiConstants.ORDER_BY) String orderBy){
        ProductResponse productResponse = productService.productsByCategory(categoryId,pageNumber,pageSize,sortBy,orderBy);
        return ResponseEntity.ok(productResponse);
    }
   @GetMapping("/public/products/keyword/{keyword}")
    ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,
   @RequestParam(name = "pageNumber" ,required = false) Integer pageNumber,
                                                        @RequestParam(name = "pageSize" ,required = false, defaultValue = ProductApiConstants.PAGE_SIZE) Integer pageSize,
                                                        @RequestParam(name = "sortBy" ,required = false,defaultValue = ProductApiConstants.SORT_BY) String sortBy,
                                                        @RequestParam(name = "orderBy" ,required = false, defaultValue = ProductApiConstants.ORDER_BY) String orderBy){
       ProductResponse response =productService.searchByKeyword(keyword,pageNumber,pageSize,sortBy,orderBy);
       return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/admin/products/{productId}")
    ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDto,@PathVariable Long productId){
             ProductDTO updatedProduct=productService.updateProduct(productId,productDto);
             return ResponseEntity.ok().body(updatedProduct);
    }
    @DeleteMapping("/admin/product/{productId}")
    ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
         ProductDTO productDTO=productService.deleteProduct(productId);
        return ResponseEntity.ok().body(productDTO);
    }
    @PutMapping("/product/{productId}/image")
    ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId ,
                                                  @RequestParam("image") MultipartFile image) throws IOException {
       ProductDTO updatedImageProduct= productService.updateProductImage(productId,image);
        return ResponseEntity.ok().body(updatedImageProduct);
    }
}
