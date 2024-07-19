package umc.unimade.global.common.exception;

import umc.unimade.global.common.BaseErrorCode;

public class ReviewExceptionHandler extends CustomException{

    public ReviewExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
