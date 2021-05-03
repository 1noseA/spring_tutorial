package mrs.domain.service.room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.MeetingRoom;
import mrs.domain.model.ReservableRoom;
import mrs.domain.repository.reservation.MeetingRoomRepository;
import mrs.domain.repository.room.ReservableRoomRepository;

// Serviceクラスとして扱うアノテーション
@Service
// このクラスの各メソッドが自動でトランザクション管理されるようにする
@Transactional
public class RoomService {

	@Autowired
	MeetingRoomRepository meetingRoomRepository;

	// 会議情報取得処理
	public MeetingRoom findMeetingRoom(Integer roomId) {
		return meetingRoomRepository.getOne(roomId);
	}

	// ReservableRoomRepositoryをインジェクションする
	@Autowired
	ReservableRoomRepository reservableRoomRepository;

	public List<ReservableRoom> findReservableRooms(LocalDate date) {
		// ReservableRoomRepositoryのメソッドを呼び出す
		return reservableRoomRepository.findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(date);
	}
}
