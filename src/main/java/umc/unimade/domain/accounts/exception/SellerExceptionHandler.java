package umc.unimade.domain.accounts.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class SellerExceptionHandler extends CustomException {
    public SellerExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
