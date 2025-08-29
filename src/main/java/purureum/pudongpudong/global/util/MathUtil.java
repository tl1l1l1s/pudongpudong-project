package purureum.pudongpudong.global.util;

import purureum.pudongpudong.global.common.constants.AppConstants;

public class MathUtil {
	
	public static double roundToTwoDecimalPlaces(double value) {
		return Math.round(value * AppConstants.DECIMAL_MULTIPLIER) / AppConstants.DECIMAL_MULTIPLIER;
	}
	
	public static double calculatePace(Integer duration, Double distance) {
		if (distance == null || distance <= 0 || duration == null || duration <= 0) {
			return 0.0;
		}
		return roundToTwoDecimalPlaces((double) duration / distance);
	}
	
	public static double calculateCompletionPercentage(int collected, int total) {
		if (total <= 0) {
			return 0.0;
		}
		return roundToTwoDecimalPlaces((double) collected / total * 100);
	}
	
	private MathUtil() {
	}
}