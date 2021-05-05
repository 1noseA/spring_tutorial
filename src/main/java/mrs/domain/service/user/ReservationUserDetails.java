package mrs.domain.service.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import mrs.domain.model.User;

/**
 * mrs.domain.model.Userを内包したUserDetails実装クラス
 */
public class ReservationUserDetails implements UserDetails {
	// model.Userを内包する。基本的なユーザ情報はこのフィールドが持つ
	private final User user;

	public ReservationUserDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// RoleName型のenumをSpringSecurityのGrantedAuthorityに変換する。プレフィックスとして"ROLE_"をつける
		return AuthorityUtils.createAuthorityList("ROLE_" + this.user.getRoleName().name());
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserId();
	}

	// アカウント切れ、アカウントロック、パスワード有効期限切れ、アカウント無効化に関するプロパティは使用しない
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
