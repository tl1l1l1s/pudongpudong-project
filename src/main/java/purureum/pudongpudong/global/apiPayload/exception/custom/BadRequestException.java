package purureum.pudongpudong.global.apiPayload.exception.custom;

import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.GeneralException;

public class BadRequestException extends GeneralException {
    public BadRequestException(final ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
