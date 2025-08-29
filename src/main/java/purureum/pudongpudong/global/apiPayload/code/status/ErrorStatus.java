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
    
    // 인증 관련 응답
    INVALID_AUTH_DATA(HttpStatus.BAD_REQUEST, "AUTH4001", "잘못된 인증 데이터입니다."),
    TOKEN_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5001", "토큰 생성에 실패했습니다."),
    
    // 공원 관련 응답
    PARK_NOT_FOUND(HttpStatus.NOT_FOUND, "PARK4001", "공원을 찾을 수 없습니다."),
    
    // 트레이너 관련 응답
    TRAINER_NOT_FOUND(HttpStatus.NOT_FOUND, "TRAINER4001", "트레이너를 찾을 수 없습니다."),
    USER_TRAINER_NOT_FOUND(HttpStatus.NOT_FOUND, "TRAINER4002", "사용자의 트레이너 정보를 찾을 수 없습니다."),
    TRAINER_PARK_NOT_ASSIGNED(HttpStatus.BAD_REQUEST, "TRAINER4003", "트레이너에 공원이 할당되지 않았습니다."),
    
    // 러닝 세션 관련 응답
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION4001", "러닝 세션을 찾을 수 없습니다."),
    INVALID_SESSION_DATA(HttpStatus.BAD_REQUEST, "SESSION4002", "잘못된 세션 데이터입니다."),
    
    // 스탬프/수목 관련 응답
    SPECIES_NOT_FOUND(HttpStatus.NOT_FOUND, "SPECIES4001", "수목 정보를 찾을 수 없습니다."),
    NO_SPECIES_IN_PARK(HttpStatus.NOT_FOUND, "SPECIES4002", "해당 공원에 등록된 수목이 없습니다."),
    
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