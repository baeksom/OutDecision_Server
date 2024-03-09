package umc.easyexcel.apiPayload.exception.handler;

import umc.easyexcel.apiPayload.code.BaseErrorCode;
import umc.easyexcel.apiPayload.exception.GeneralException;

public class FunctionsHandler extends GeneralException {

    public FunctionsHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
