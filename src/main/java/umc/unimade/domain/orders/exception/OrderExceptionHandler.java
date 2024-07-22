package umc.unimade.domain.orders.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class OrderExceptionHandler extends CustomException {
    public OrderExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}

