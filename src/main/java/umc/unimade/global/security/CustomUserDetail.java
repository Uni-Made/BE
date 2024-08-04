package umc.unimade.global.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class CustomUserDetail implements UserDetails {

    private final String email;
    private final List<GrantedAuthority> authorities; // 사용자의 권한 목록을 저장하는 필드

    // 생성자: 사용자 ID와 권한 목록을 받아서 초기화
    public CustomUserDetail(String email, List<GrantedAuthority> authorities) {
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    // UserLoginForm 객체로부터 CustomUserDetail 객체를 생성하는 정적 팩토리 메서드
    public static CustomUserDetail create(UserLoginForm user) {
        // 사용자의 역할을 기반으로 권한 목록을 생성
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // 생성자를 호출하여 CustomUserDetail 객체를 반환
        return new CustomUserDetail(user.getId(), authorities);
    }
}