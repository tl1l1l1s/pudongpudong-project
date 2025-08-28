package purureum.pudongpudong.global.apiPayload.exception.custom;

import purureum.pudongpudong.global.apiPayload.code.BaseErrorCode;
import purureum.pudongpudong.global.apiPayload.exception.GeneralException;

public class UserNotFoundException extends GeneralException {
	public UserNotFoundException(BaseErrorCode errorCode) {
		super(errorCode);
	}
}