package umc.unimade.domain.accounts.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class UserExceptionHandler extends CustomException {

    public UserExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
