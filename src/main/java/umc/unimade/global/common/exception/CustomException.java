package umc.unimade.global.common.exception;

import lombok.Getter;
import umc.unimade.global.common.BaseErrorCode;

@Getter
public class CustomException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
