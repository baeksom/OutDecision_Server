package KGUcapstone.OutDecision.global.error.handler;

import KGUcapstone.OutDecision.global.error.dto.BaseErrorCode;
import KGUcapstone.OutDecision.global.error.exception.GeneralException;

public class MemberHandler extends GeneralException {

    public MemberHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
