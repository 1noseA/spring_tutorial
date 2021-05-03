package mrs.domain.repository.room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;

public interface ReservableRoomRepository extends JpaRepository<ReservableRoom, ReservableRoomId>{
	// 指定日に予約可能な会議室の一覧を取得するメソッドを定義
	// 日付を指定してreservable_roomテーブルからroom_idの昇順でデータを取得する
	List<ReservableRoom> findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(LocalDate reservedDate);
}
