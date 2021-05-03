package mrs.domain.service.reservation;

public class AlreadyReservedException extends RuntimeException {
	// 入力された日付・部屋の組み合わせはすでに予約済みであること
	public AlreadyReservedException(String message) {
		super(message);
	}
}
