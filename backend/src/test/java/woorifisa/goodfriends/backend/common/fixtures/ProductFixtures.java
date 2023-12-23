package woorifisa.goodfriends.backend.common.fixtures;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import woorifisa.goodfriends.backend.product.domain.Product;
import woorifisa.goodfriends.backend.product.domain.ProductCategory;
import woorifisa.goodfriends.backend.product.domain.ProductStatus;
import woorifisa.goodfriends.backend.product.dto.request.ProductCreateRequest;
import woorifisa.goodfriends.backend.product.dto.response.ProductResponse;
import woorifisa.goodfriends.backend.product.dto.response.ProductsResponse;

import java.util.List;

import static woorifisa.goodfriends.backend.common.fixtures.ProfileFixtures.*;
import static woorifisa.goodfriends.backend.common.fixtures.UserFixtures.*;

public class ProductFixtures {

    // 상품 제목
    public static final String 상품제목1 = "아이패드";
    public static final String 상품제목2 = "티셔츠";
    public static final String 상품제목3 = "화분";

    //상품 상세 설명
    public static final String 상세_설명1 = "최신형 아이패드 팔아요.";
    public static final String 상세_설명2 = "실착 1회 티셔츠 입니다.";
    public static final String 상세_설명3 = "다육이 화분 1개 있습니다.";

    // 판매 가격
    public static final int 판매_가격1 = 100000;
    public static final int 판매_가격2 = 20000;
    public static final int 판매_가격3 = 5000;


    public static Product 상품1() {
        return Product.builder()
                .user(코코())
                .productCategory(ProductCategory.DIGITAL_DEVICE)
                .title(상품제목1)
                .status(ProductStatus.SELL)
                .description(상세_설명1)
                .sellPrice(판매_가격1)
                .build();
    }

    public static Product 상품2() {
        return Product.builder()
                .user(춘식())
                .productCategory(ProductCategory.CLOTHING)
                .title(상품제목2)
                .status(ProductStatus.SELL)
                .description(상세_설명2)
                .sellPrice(판매_가격2)
                .build();
    }

    public static Product 상품3() {
        return Product.builder()
                .user(고잉홈())
                .productCategory(ProductCategory.PLANTS)
                .title(상품제목3)
                .status(ProductStatus.SELL)
                .description(상세_설명3)
                .sellPrice(판매_가격3)
                .build();
    }

    public static ProductsResponse 전체_조회() {
        List<ProductResponse> responses = List.of(new ProductResponse[]{
                new ProductResponse(3L, 상품3().getProductCategory(), 상품3().getTitle(), 상품3().getStatus(), 상품3().getSellPrice(), "image3.png", 주소3, true),
                new ProductResponse(2L, 상품2().getProductCategory(), 상품2().getTitle(), 상품2().getStatus(), 상품2().getSellPrice(), "image2.png", 주소2, true),
                new ProductResponse(1L, 상품1().getProductCategory(), 상품1().getTitle(), 상품1().getStatus(), 상품1().getSellPrice(), "image1.png", 주소1, true)});
        return new ProductsResponse(responses);
    }

    final String fileName = "image1";
    final String contentType = "png";

    static MockMultipartFile image1 = new MockMultipartFile("image", "image.png", String.valueOf(MediaType.IMAGE_PNG), "image".getBytes());
    public static ProductCreateRequest 등록_요청() {
        return new ProductCreateRequest(상품1().getTitle(), 상품1().getProductCategory(), 상품1().getDescription(), 상품1().getSellPrice(), List.of(image1));
    }
}