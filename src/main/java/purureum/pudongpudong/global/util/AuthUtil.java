package purureum.pudongpudong.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.handler.ApiHandler;
import purureum.pudongpudong.global.common.constants.AppConstants;

@Component
@RequiredArgsConstructor
public class AuthUtil {
	
	private final JwtUtil jwtUtil;
	
	public Long extractUserIdFromHeader(String authorizationHeader) {
		String token = extractTokenFromHeader(authorizationHeader);
		Long userId = jwtUtil.getUserIdFromToken(token);
		
		if (userId == null) {
			throw new ApiHandler(ErrorStatus._UNAUTHORIZED);
		}
		
		return userId;
	}
	
	private String extractTokenFromHeader(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(AppConstants.BEARER_PREFIX)) {
			throw new ApiHandler(ErrorStatus._UNAUTHORIZED);
		}
		return authorizationHeader.substring(AppConstants.BEARER_PREFIX_LENGTH);
	}
}