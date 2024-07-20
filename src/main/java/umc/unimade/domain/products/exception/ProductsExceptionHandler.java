package umc.unimade.domain.products.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class ProductsExceptionHandler extends CustomException {

    public ProductsExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
