package KGUcapstone.OutDecision.global.error.exception.handler;

import KGUcapstone.OutDecision.global.error.dto.BaseErrorCode;
import KGUcapstone.OutDecision.global.error.exception.GeneralException;

public class NotificationHandler extends GeneralException {
    public NotificationHandler(BaseErrorCode errorCode) { super(errorCode); }
}
