package KGUcapstone.OutDecision.global.error.exception.handler;

import KGUcapstone.OutDecision.global.error.dto.BaseErrorCode;
import KGUcapstone.OutDecision.global.error.exception.GeneralException;

public class PostHandler extends GeneralException {
    public PostHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
