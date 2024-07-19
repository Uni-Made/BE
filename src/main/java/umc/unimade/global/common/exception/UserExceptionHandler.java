package umc.unimade.global.common.exception;

import umc.unimade.global.common.BaseErrorCode;

public class UserExceptionHandler extends CustomException{

    public UserExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
