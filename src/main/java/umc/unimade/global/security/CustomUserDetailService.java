package umc.unimade.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.global.common.exception.CustomException;
import umc.unimade.global.common.exception.GlobalErrorCode;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;



    @Override
    public UserDetails loadUserByUsername(String username) {
        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserLoginForm buyer = buyerRepository.findBySocialIdAndRefreshToken(username)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.LOGIN_ACCESS_DENIED));

        return CustomUserDetail.create(buyer);
    }

    public CustomUserDetail loadUserByUsernameAndUserRole(String username, String role) {
        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + role));

        UserLoginForm user;
        switch (role) {
            case "BUYER" :
                user = buyerRepository.findBySocialIdAndRefreshToken(username)
                        .orElseThrow(() -> new CustomException(GlobalErrorCode.LOGIN_ACCESS_DENIED));
                break;
            case "SELLER", "ADMIN":
                user = sellerRepository.findByEmailAndRefreshToken(username)
                        .orElseThrow(() -> new CustomException(GlobalErrorCode.LOGIN_ACCESS_DENIED));
                break;
            default:
                throw new CustomException(GlobalErrorCode.LOGIN_ACCESS_DENIED);
        }

        return CustomUserDetail.create(user);
    }
}
