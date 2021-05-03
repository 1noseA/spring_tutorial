package mrs.domain.service.reservation;

public class UnavailableReservationException extends RuntimeException {
	// 入力された日付・部屋の組み合わせでは予約できないこと
	public UnavailableReservationException(String message) {
		super(message);
	}
}
