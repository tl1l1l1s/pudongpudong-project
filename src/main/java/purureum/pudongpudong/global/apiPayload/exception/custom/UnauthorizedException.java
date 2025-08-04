package purureum.pudongpudong.global.apiPayload.exception.custom;

import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.GeneralException;

public class UnauthorizedException extends GeneralException {
    public UnauthorizedException(final ErrorStatus errorStatus) {
        super(errorStatus);
    }

    public ErrorStatus getErrorStatus() {
        return (ErrorStatus) super.getCode();
    }
}
