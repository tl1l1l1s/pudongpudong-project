package purureum.pudongpudong.global.apiPayload.code.status;

import purureum.pudongpudong.global.apiPayload.code.BaseErrorCode;
import purureum.pudongpudong.global.apiPayload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    
    // 사용자 관련 응답
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "사용자를 찾을 수 없습니다."),
    
    // 공원 관련 응답
    PARK_NOT_FOUND(HttpStatus.NOT_FOUND, "PARK4001", "공원을 찾을 수 없습니다."),
    
    // 외부 API 관련 응답
    KAKAO_MAP_API_BAD_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "KAKAO4001", "카카오 맵 API 호출을 잘못된 요청으로 인해 실패했습니다."),
    KAKAO_MAP_API_SERVER_ERROR(HttpStatus.BAD_REQUEST, "KAKAO5001", "카카오 맵 API 호출을 서버 에러로 인해 실패했습니다."),
    KAKAO_NAVI_API_BAD_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "KAKAO4002", "카카오 네비 API 호출을 잘못된 요청으로 인해 실패했습니다."),
    KAKAO_NAVI_API_SERVER_ERROR(HttpStatus.BAD_REQUEST, "KAKAO5002", "카카오 네비 API 호출을 서버 에러로 인해 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }

}