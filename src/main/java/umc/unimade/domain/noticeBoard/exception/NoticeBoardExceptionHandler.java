package umc.unimade.domain.noticeBoard.exception;

import umc.unimade.global.common.BaseErrorCode;
import umc.unimade.global.common.exception.CustomException;

public class NoticeBoardExceptionHandler extends CustomException {

    public NoticeBoardExceptionHandler (BaseErrorCode code ){
        super(code);
    }
}
