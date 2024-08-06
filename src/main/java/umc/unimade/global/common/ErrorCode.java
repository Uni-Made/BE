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
    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "USER4001", "관리자만 접근할 수 있습니다."),
    LOGIN_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "USER4002", "로그인이 필요합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4003", "사용자를 찾을 수 없습니다."),

    // Products 관련 에러
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT4000", "해당 제품을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4001", "해당 카테고리를 찾을 수 없습니다."),

    // Products 관련 에러
    PRODUCT_STATUS_IS_NOT_PENDING(HttpStatus.BAD_REQUEST, "PRODUCT4001", "대기중인 요청만 처리할 수 있습니다."),
    PRODUCT_STATUS_IS_NOT_PENDING_OR_HOLD(HttpStatus.BAD_REQUEST, "PRODUCT4002", "대기중 이거나 보류중인 요청만 승인할 수 있습니다."),
    PRODUCT_STATUS_IS_NOT_SOLDOUT(HttpStatus.BAD_REQUEST, "PRODUCT4003", "판매 중단 상품만 판매 재등록할 수 있습니다."),
    PRODUCT_STATUS_IS_NOT_SELLING(HttpStatus.BAD_REQUEST, "PRODUCT4004", "판매 중인 상품만 수정할 수 있습니다."),

    // Order 관련 에러
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER4000", "구매 내역을 찾을 수 없습니다."),
    STATUS_IS_PENDING(HttpStatus.BAD_REQUEST, "ORDER4001", "아직 입금되지 않은 대기 중 상태입니다."),
    ORDER_AGREEMENT_REQUIRED(HttpStatus.BAD_REQUEST,"ORDER4002","동의는 필수 항목입니다."),
    PHONE_NUMBER_REQUIRED(HttpStatus.BAD_REQUEST,"ORDER4002","전화번호는 필수 항목입니다."),
    NAME_REQUIRED(HttpStatus.BAD_REQUEST,"ORDER4002","이름은 필수 항목입니다."),
    PURCHASE_FORM_REQUIRED(HttpStatus.BAD_REQUEST,"ORDER4002","구매폼은 필수입니다."),
    ADDRESS_REQUIRED(HttpStatus.BAD_REQUEST,"ORDER4002","주소는 필수 항목입니다."),
    OPTION_REQUIRED(HttpStatus.BAD_REQUEST,"ORDER4002","옵션을 선택해주세요."),
    COUNT_REQUIRED(HttpStatus.BAD_REQUEST,"ORDER4002","수량은 1개 이상 선택해주세요."),
    INVALID_ORDER_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "ORDER5000", "서버 에러"),


    // 판매자 관련 에러
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "SELLER4000", "판매자를 찾을 수 없습니다."),
    SELLER_STATUS_IS_NOT_PENDING(HttpStatus.BAD_REQUEST, "SELLER4001", "대기중인 요청만 처리할 수 있습니다."),
    SELLER_STATUS_IS_NOT_PENDING_OR_HOLD(HttpStatus.BAD_REQUEST, "SELLER4002", "대기중 이거나 보류중인 요청만 승인할 수 있습니다."),

    // 구매자 관련 에러
    BUYER_NOT_FOUND(HttpStatus.NOT_FOUND, "Buyer4000", "구매자를 찾을 수 없습니다."),


    // 리뷰 관련 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW4000", "리뷰를 찾을 수 없습니다."),
    INVALID_RATING_STAR(HttpStatus.BAD_REQUEST, "REVIEW4001", "별점은 0점 이상이어야합니다"),
    REVIEW_CREATE_NOT_BUYER(HttpStatus.FORBIDDEN,"REVIEW4003","리뷰를 작성할 권한이 없습니다."),
    REVIEW_DELETE_NOT_OWNER(HttpStatus.FORBIDDEN,"REVIEW4003","리뷰를 삭제할 권한이 없습니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW4004", "리뷰 신고를 찾을 수 없습니다."),


    // QnA 관련 에러
    QNA_NOT_FOUND(HttpStatus.NOT_FOUND, "QNA4000", "QNA를 찾을 수 없습니다."),
    QUESTION_DELETE_NOT_OWNER(HttpStatus.FORBIDDEN,"QNA4003","질문을 삭제할 권한이 없습니다."),
    ANSWER_DELETE_NOT_OWNER(HttpStatus.FORBIDDEN,"QNA4003","답변 삭제할 권한이 없습니다."),

    
    //토큰 관련 에러
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN4000", "토큰이 만료되었습니다."),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "TOKEN4001", "지원되지 않는 토큰입니다."),
    TOKEN_UNKNOWN(HttpStatus.UNAUTHORIZED, "TOKEN4002", "알 수 없는 토큰입니다."),
    TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "TOKEN4003", "토큰 타입이 잘못되었습니다."),
    TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED, "TOKEN4004", "토큰이 잘못되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN4005", "토큰이 유효하지 않습니다."),
    
    //sms 관련 오류

    SMS_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SMS4000", "SMS 전송에 실패했습니다."),
    SMS_VERIFY_FAILED(HttpStatus.BAD_REQUEST, "SMS4001", "인증번호가 일치하지 않습니다.");
    
    

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}