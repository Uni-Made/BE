package umc.unimade.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "ERROR4000", "입력값에 대한 검증에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR5000", "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요."),

    LOGIN_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "ERROR4010", "로그인이 필요합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR4040", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "ERROR4090", "이미 존재하는 사용자입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ERROR4013", "토큰이 만료되었습니다."),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "ERROR4015", "지원되지 않는 토큰입니다."),
    TOKEN_UNKNOWN(HttpStatus.UNAUTHORIZED, "ERROR4016", "알 수 없는 토큰입니다."),
    TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "ERROR4014", "토큰 타입이 잘못되었습니다."),
    TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED, "ERROR4011", "토큰이 잘못되었습니다."),
    SMS_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR5001", "SMS 전송에 실패했습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "ERROR4011", "토큰이 유효하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }

}