package mrs.domain.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class ReservableRoom implements Serializable {
	// 複合主キーを使うために複合主キークラスであるResarvableRoomIdを用意してアノテーション付与
	@EmbeddedId
	private ReservableRoomId reservableRoomId;

	// MeetingRoomエンティティと1方向の多対1の関連設定を行う。外部キーとしてroom_idを指定
	@ManyToOne
	// このフィールドに対する値の変更がデータベースに反映されないようにinsertableとupdatable属性はfalse
	@JoinColumn(name = "room_id", insertable = false, updatable = false)
	// MapsIdに複合クラスのうち外部キーとして使うフィールド名を指定
	@MapsId("roomId")
	private MeetingRoom meetingRoom;

	public ReservableRoom(ReservableRoomId reservableRoomId) {
		this.reservableRoomId = reservableRoomId;
	}

	public ReservableRoom() {
	}

	public ReservableRoomId getReservableRoomId() {
		return reservableRoomId;
	}

	public void setReservableRoomId(ReservableRoomId reservableRoomId) {
		this.reservableRoomId = reservableRoomId;
	}

	public MeetingRoom getMeetingRoom() {
		return meetingRoom;
	}

	public void setMeetingRoom(MeetingRoom meetingRoom) {
		this.meetingRoom = meetingRoom;
	}
}
