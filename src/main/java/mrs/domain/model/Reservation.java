package mrs.domain.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
public class Reservation implements Serializable {
	@Id
	// 主キーをJPAに自動採番させる。PostgreSQLの場合は該当するカラムにserial型が使用される
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reservationId;

	private LocalTime startTime;

	private LocalTime endTime;

	// ReservableRoomエンティティと1方向の多対1の関連設定を行う。
	// 外部キーとしてreserved_dateとroom_idの複合キーを指定する
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "reserved_date"),
		@JoinColumn(name = "room_id") })
	private ReservableRoom reservableRoom;

	// Userエンティティと1方向の多対1の関連設定を行う。外部キーとしてuser_idを指定
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public boolean overlap(Reservation target) {
		// 2つの予約の日付・部屋が別であれば重複していないためfalse
		if (!Objects.equals(reservableRoom.getReservableRoomId(), target.reservableRoom.getReservableRoomId())) {
			return false;
		}
		// 2つの予約の開始時刻と終了時刻が一致する場合は重複であるためtrue
		if (startTime.equals(target.startTime) && endTime.equals(target.endTime)) {
			return true;
		}
		// 2つの予約の開始時刻と終了時刻が交差しているか、または包括関係にあるかを返す
		return target.endTime.isAfter(startTime) && endTime.isAfter(target.startTime);
	}


	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public ReservableRoom getReservableRoom() {
		return reservableRoom;
	}

	public void setReservableRoom(ReservableRoom reservableRoom) {
		this.reservableRoom = reservableRoom;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
