package com.JtoP.Spring.boundedContext.user.service;


import com.JtoP.Spring.boundedContext.user.entity.SiteUser;
import com.JtoP.Spring.boundedContext.user.enums.UserRole;
import com.JtoP.Spring.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor // final이 붙은 필드의 생성자를 자동으로 만들어 준다.
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    // 시큐리티가 특정 회원을 username으로 찾을 때 호출 되는 메서드
    // 해당 username에 해당되는 회원 정보를 얻는 수단
    // 시큐리티는 siteUser 테이블이 존재하는지 모른다.
    // 아래 코드 정도는 구현을 해야 한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteUser = userRepository.findByUsername(username);

        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        SiteUser siteUser = _siteUser.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            // 관리자 권한 부여
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
            // 일반 회원 권한 부여
        }

        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}
