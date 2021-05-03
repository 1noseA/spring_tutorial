package mrs.domain.repository.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	// 指定した会議室の予約一覧を取得するメソッドを定義する
	List<Reservation> findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(ReservableRoomId reservableRoomId);
}
