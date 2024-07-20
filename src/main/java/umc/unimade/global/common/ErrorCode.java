package umc.unimade.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400-", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON4001", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4003", "금지된 요청입니다."),

    // Accounts 관련 에러
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER4000", "사용자가 이미 존재합니다."),
    BUYER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4040", "구매자를 찾을 수 없습니다."),

    // Products 관련 에러
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT4000", "해당 제품을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4000", "해당 카테고리를 찾을 수 없습니다."),

    // 판매자 관련 에러
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4040", "판매자를 찾을 수 없습니다."),

    // 구매자 관련 에러

    // 리뷰 관련 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW4040", "리뷰를 찾을 수 없습니다."),

    // QnA 관련 에러
    QNA_NOT_FOUND(HttpStatus.NOT_FOUND, "QNA4040", "QNA를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}