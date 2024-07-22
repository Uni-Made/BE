package umc.unimade.domain.orders.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class SellerExceptionhandler extends CustomException {

    public SellerExceptionhandler (BaseErrorCode code ){
        super(code);
    }
}