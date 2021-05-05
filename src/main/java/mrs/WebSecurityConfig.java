package mrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import mrs.domain.service.user.ReservationUserDetailsService;

@Configuration
// SpringSecurityのWeb連携機能を有効にする
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	ReservationUserDetailsService userDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
		// パスワードのエンコードアルゴリズム
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		// /js以下と/css以下へのアクセスは常に許可(permitAll)
		.antMatchers("/js/**", "/css/**").permitAll()
		// それ以外は認証を要求する(authenticated)
		.antMatchers("/**").authenticated()
		.and()
		// フォーム認証を行う。ログイン画面、認証URL、ユーザ名・パスワードのリクエストパラメータ名、認証成功時・失敗時の遷移先設定
		.formLogin()
		.loginPage("/loginForm")
		.loginProcessingUrl("/login")
		.usernameParameter("username")
		.passwordParameter("password")
		// trueにすることで認証成功時は常に指定したパスへ遷移する
		.defaultSuccessUrl("/rooms", true)
		// ログイン画面、認証URL、認証失敗時の遷移先へのアクセスは常に許可
		.failureUrl("/loginForm?error=true").permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 指定のUserDetailsServiceとPasswordEncoderを使用して認証を行う
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}
