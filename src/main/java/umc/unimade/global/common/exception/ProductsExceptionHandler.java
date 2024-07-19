package umc.unimade.global.common.exception;

import umc.unimade.global.common.BaseErrorCode;

public class ProductsExceptionHandler extends CustomException{

    public ProductsExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
