package umc.unimade.global.common.exception;

import umc.unimade.global.common.BaseErrorCode;

public class QnAExceptionHandler extends CustomException{

    public QnAExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
