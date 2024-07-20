package umc.unimade.domain.accounts.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class BuyerExceptionHandler extends CustomException {
    public BuyerExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
