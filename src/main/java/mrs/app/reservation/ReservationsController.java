package mrs.app.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.service.reservation.AlreadyReservedException;
import mrs.domain.service.reservation.ReservationService;
import mrs.domain.service.reservation.UnavailableReservationException;
import mrs.domain.service.room.RoomService;
import mrs.domain.service.user.ReservationUserDetails;

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
		// model.addAttribute("user", dummyUser());
		return "reservation/reserveForm";
	}

//	private User dummyUser() {
//		User user = new User();
//		user.setUserId("taro-yamada");
//		user.setFirstName("太郎");
//		user.setLastName("山田");
//		user.setRoleName(RoleName.USER);
//		return user;
//	}

	// 予約処理
	@RequestMapping(method = RequestMethod.POST)
	// @Validatedをつけると入力チェックが行われる
	String reserve(@Validated ReservationForm form, BindingResult bindingResult,
			// 認証済みのUserDetailsオブジェクトを取得する
			@AuthenticationPrincipal ReservationUserDetails userDetails,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			@PathVariable("roomId") Integer roomId, Model model) {
		// bindingResult.hasErrorsメソッドの入力チェックエラーがあるかどうかを確認できる
		if (bindingResult.hasErrors()) {
			// エラーがある場合はフォーム表示画面へ遷移
			return reserveForm(date, roomId, model);
		}

		ReservableRoom reservableRoom = new ReservableRoom(new ReservableRoomId(roomId, date));
		Reservation reservation = new Reservation();
		reservation.setStartTime(form.getStartTime());
		reservation.setEndTime(form.getEndTime());
		reservation.setReservableRoom(reservableRoom);
		// ReservationUserDetailsオブジェクトから認証済みのUserオブジェクトを取得し予約処理に使用する
		reservation.setUser(userDetails.getUser());

		try {
			reservationService.reserve(reservation);
		} catch (UnavailableReservationException | AlreadyReservedException e) {
			// 例外発生時は例外メッセージを画面に表示させるためModelに設定
			model.addAttribute("error", e.getMessage());
			return reserveForm(date, roomId, model);
		}
		// 予約に成功した場合は予約一覧表示画面へリダイレクトする
		return "redirect:/reservations/{date}/{roomId}";
	}

	// 予約取り消し処理
	@RequestMapping(method = RequestMethod.POST, params = "cancel")
	// 認証済みのUserDetailsオブジェクトを取得する
	String cancel(
			// @AuthenticationPrincipal ReservationUserDetails userDetails,
			@RequestParam("reservationId") Integer reservationId,
			@PathVariable("roomId") Integer roomId,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			Model model) {
		// ReservationUserDetailsオブジェクトから認証済みのUserオブジェクトを取得し予約処理に使用する
		// User user = userDetails.getUser();
		try {
			Reservation reservation = reservationService.findOne(reservationId);
			reservationService.cancel(reservation);
		} catch (AccessDeniedException e) {
			model.addAttribute("error", e.getMessage());
			return reserveForm(date, roomId, model);
		}
		return "redirect:/reservations/{date}/{roomId}";
	}
}
