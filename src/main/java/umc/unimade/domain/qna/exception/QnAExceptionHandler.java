package umc.unimade.domain.qna.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class QnAExceptionHandler extends CustomException {

    public QnAExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
