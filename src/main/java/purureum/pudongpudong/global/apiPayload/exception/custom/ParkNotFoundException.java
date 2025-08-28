package purureum.pudongpudong.global.apiPayload.exception.custom;

import purureum.pudongpudong.global.apiPayload.code.BaseErrorCode;
import purureum.pudongpudong.global.apiPayload.exception.GeneralException;

public class ParkNotFoundException extends GeneralException {
	public ParkNotFoundException(BaseErrorCode errorCode) {
		super(errorCode);
	}
}