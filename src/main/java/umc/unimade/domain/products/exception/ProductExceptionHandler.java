package umc.unimade.domain.products.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class ProductExceptionHandler extends CustomException {
    public ProductExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
