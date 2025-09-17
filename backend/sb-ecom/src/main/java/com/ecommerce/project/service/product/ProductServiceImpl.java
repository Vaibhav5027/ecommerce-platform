package com.ecommerce.project.service.product;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.product.ProductDTO;
import com.ecommerce.project.payload.product.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.service.File.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
   private FileService fileService;

    @Value("${product.filepath}")
    private String path;


    @Override
    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId).orElseThrow(() -> new ResourceNotFoundException("category", "CategoryId", categoryId));
        List<Product> products = category.getProduct();
         boolean isProductPresent=true;

         for(Product product:products){
             if(product.getProductName().equalsIgnoreCase(productDto.getProductName())){
                 isProductPresent=false;
                 break;
             }

         }
        if(isProductPresent){
            Product product = modelMapper.map(products, Product.class);
            product.setCategory(category);
            product.setImage("default.png");
            product.setSpecialPrice(product.getPrice()-(product.getPrice()*product.getDiscount()/100));
            Product save = productRepository.save(product);
            return modelMapper.map(save, ProductDTO.class);
        }
        else{
            throw new APIException("product already exist!!");
        }

    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        Sort sort=orderBy.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<ProductDTO> result = productPage.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse response=new ProductResponse();
        response.setContent(result);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalPages(productPage.getTotalPages());
        response.setTotalElements(productPage.getTotalElements());
        response.setLastPage(productPage.isLast());
        return response;
    }

    @Override
    public ProductResponse productsByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String orderBy) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
        Sort sort=orderBy.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceDesc(category,pageDetails);

        List<ProductDTO> list = productPage.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
       ProductResponse response=new ProductResponse();
        response.setContent(list);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalPages(productPage.getTotalPages());
        response.setTotalElements(productPage.getTotalElements());
        response.setLastPage(productPage.isLast());
        return response;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String orderBy) {
        Sort sort=orderBy.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%",pageDetails);

        List<ProductDTO> list = productPage.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse response=new ProductResponse();
        response.setContent(list);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalPages(productPage.getTotalPages());
        response.setTotalElements(productPage.getTotalElements());
        response.setLastPage(productPage.isLast());
        return response;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        product.setDiscount(productDto.getDiscount());
        product.setImage("default.png");
        product.setSpecialPrice(productDto.getPrice()-(productDto.getPrice()*productDto.getDiscount()/100));
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty())
            throw new APIException("cannot find id with : "+productId);
        ProductDTO deleted = modelMapper.map(product.get(), ProductDTO.class);
        productRepository.delete(product.get());
        return deleted;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile file) throws IOException {
         Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
         String filename=  fileService.uploadImage(file,path);
         product.setImage(filename);
        return modelMapper.map(product, ProductDTO.class);

    }




}
