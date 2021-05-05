package mrs.app.reservation;

import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ThirtyMinutesUnitValidator implements ConstraintValidator<ThirtyMinutesUnit, LocalTime> {
	@Override
	public void initialize(ThirtyMinutesUnit constraintAnnotation) {

	}

	@Override
	public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
		// 入力値がnullの場合はこのValidatorではチェックせず@NotNullなどに委譲する
		if (value == null) {
			return true;
		}
		// 分が30で割り切れるかどうかをチェックする
		return value.getMinute() % 30 == 0;
	}
}
