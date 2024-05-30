package KGUcapstone.OutDecision.global.error.exception.handler;

import KGUcapstone.OutDecision.global.error.dto.BaseErrorCode;
import KGUcapstone.OutDecision.global.error.exception.GeneralException;

public class OptionHandler extends GeneralException {
    public OptionHandler(BaseErrorCode errorCode) { super(errorCode); }
}
