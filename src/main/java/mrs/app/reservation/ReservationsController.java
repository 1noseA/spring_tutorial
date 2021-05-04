package mrs.app.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.model.RoleName;
import mrs.domain.model.User;
import mrs.domain.service.reservation.ReservationService;
import mrs.domain.service.room.RoomService;

@Controller
// リクエストパスからdateとroomIdを取れるようにする
@RequestMapping("reservations/{date}/{roomId}")
public class ReservationsController {
	@Autowired
	RoomService roomService;
	@Autowired
	ReservationService reservationService;

	// 各リクエストのModelに格納するオブジェクトを作成する
	@ModelAttribute
	ReservationForm serUpForm() {
		ReservationForm form = new ReservationForm();
		// デフォルト値
		form.setStartTime(LocalTime.of(9, 0));
		form.setEndTime(LocalTime.of(10, 0));
		return form;
	}

	@RequestMapping(method = RequestMethod.GET)
	String reserveForm(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	// リクエストパス内の{data}に相当する部分をLocalDateオブジェクトにバインドする
	@PathVariable("date") LocalDate date,
	@PathVariable("roomId") Integer roomId, Model model) {
		ReservableRoomId reservableRoomId = new ReservableRoomId(roomId, date);
		List<Reservation> reservations = reservationService.findReservations(reservableRoomId);

		// 00:00~23:30まで30分単位でLocalDateオブジェクトを作成しリストに格納する
		List<LocalTime> timeList =
				// 30分刻みの無限ストリーム
				Stream.iterate(LocalTime.of(0, 0), t -> t.plusMinutes(30))
				// 48個で止めている
				.limit(24 * 2)
				.collect(Collectors.toList());

		model.addAttribute("room", roomService.findMeetingRoom(roomId));
		model.addAttribute("reservations", reservations);
		model.addAttribute("timeList", timeList);
		// 予約ユーザとしてダミーを設定する
		model.addAttribute("user", dummyUser());
		return "reservation/reserveForm";
	}

	private User dummyUser() {
		User user = new User();
		user.setUserId("taro-yamada");
		user.setFirstName("太郎");
		user.setLastName("山田");
		user.setRoleName(RoleName.USER);
		return user;
	}
}
