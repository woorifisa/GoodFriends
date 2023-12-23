package woorifisa.goodfriends.backend.product.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import woorifisa.goodfriends.backend.auth.dto.LoginUser;
import woorifisa.goodfriends.backend.auth.presentation.AuthenticationPrincipal;
import woorifisa.goodfriends.backend.product.application.ProductService;
import woorifisa.goodfriends.backend.product.domain.ProductCategory;
import woorifisa.goodfriends.backend.product.dto.request.ProductCreateRequest;
import woorifisa.goodfriends.backend.product.dto.request.ProductUpdateRequest;
import woorifisa.goodfriends.backend.product.dto.response.ProductUpdateResponse;
import woorifisa.goodfriends.backend.product.dto.response.ProductDetailResponse;
import woorifisa.goodfriends.backend.product.dto.response.ProductsResponse;
import woorifisa.goodfriends.backend.user.application.UserService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

@RequestMapping("/api/products")
@RestController
public class ProductController {

    private static final int PAGE_SIZE = 12;
    private final ProductService productService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
    }

    // 상품 등록
    @PostMapping
    public ResponseEntity<Void> saveProduct(@AuthenticationPrincipal final LoginUser loginUser,
                                            @RequestPart ProductCreateRequest request,
                                            @RequestPart List<MultipartFile> multipartFiles) throws IOException {
            ProductCreateRequest productCreateRequest = new ProductCreateRequest(request.getTitle(), request.getProductCategory(),
                                                            request.getDescription(), request.getSellPrice(), multipartFiles);
            Long productId = productService.saveProduct(loginUser.getId(), productCreateRequest);
            return ResponseEntity.created(URI.create("/products/" + productId)).build(); // 201
    }

    // 상품 검색
    @GetMapping("/search")
    public ResponseEntity<ProductsResponse> findSearchProduct(@PageableDefault(size=PAGE_SIZE) Pageable pageable,
                                                              @RequestParam String productCategory,
                                                              @RequestParam String keyword) {
        ProductsResponse responses = productService.findSearchProduct(pageable, productCategory, keyword);
        return ResponseEntity.ok().body(responses); // 200
    }

    // 상품 카테고리별 조회
    @GetMapping("/category")
    public ResponseEntity<ProductsResponse> findProductByCategory(@PageableDefault(size=PAGE_SIZE) Pageable pageable,
                                                                  @RequestParam String productCategory) {
        ProductCategory category = ProductCategory.valueOf(productCategory);
        ProductsResponse responses = productService.findProductByCategory(pageable, category);
        return ResponseEntity.ok().body(responses); // 200
    }

    // 상품 전체 조회
    @GetMapping
    public ResponseEntity<ProductsResponse> findAllProducts(@PageableDefault(size=PAGE_SIZE) Pageable pageable) {
        ProductsResponse responses = productService.findAllProducts(pageable);
        return ResponseEntity.ok().body(responses); // 200
    }

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> findProduct(@AuthenticationPrincipal final LoginUser loginUser,
                                                                @PathVariable Long productId) {
        ProductDetailResponse response = productService.findProduct(loginUser.getId(), productId);
        return ResponseEntity.ok().body(response); // 200
    }

    // 수정할 상품 상세 조회
    @GetMapping("/edit/{productId}")
    public ResponseEntity<ProductUpdateResponse> findEditProduct(@AuthenticationPrincipal final LoginUser loginUser,
                                                                 @PathVariable Long productId){
            ProductUpdateResponse response = productService.findEditProduct(loginUser.getId(), productId);
            return ResponseEntity.ok().body(response); // 200
    }

    // 상품 수정
    @PutMapping("/edit/{productId}")
    public ResponseEntity<Void> updateProduct(@AuthenticationPrincipal final LoginUser loginUser,
                                              @PathVariable Long productId,
                                              @RequestPart ProductUpdateRequest request,
                                              @RequestPart List<MultipartFile> multipartFiles) throws IOException {
            ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(request.getTitle(), request.getProductCategory(), request.getDescription(), request.getSellPrice(), multipartFiles);
            productService.updateProduct(productUpdateRequest, loginUser.getId(), productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    // 상품 삭제
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> deleteProduct(@AuthenticationPrincipal final LoginUser loginUser,
                                              @PathVariable Long productId) throws MalformedURLException {
            productService.deleteById(loginUser.getId(), productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

}