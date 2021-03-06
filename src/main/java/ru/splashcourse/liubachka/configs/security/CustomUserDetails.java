package ru.splashcourse.liubachka.configs.security;

import java.util.Collection;
import java.util.stream.Collectors;

import ru.splashcourse.liubachka.logics.admin.usermanagment.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * CustomUserDetails
 */
@SuppressWarnings("serial")
public class CustomUserDetails extends User implements UserDetails {

	/**
	 * CustomUserDetails
	 * 
	 * @param user
	 *            user
	 */
	public CustomUserDetails(final User user) {
		super(user);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

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
		return super.isEnabled();
	}

}
