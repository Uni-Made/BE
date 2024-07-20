package umc.unimade.domain.review.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class ReviewExceptionHandler extends CustomException {

    public ReviewExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
