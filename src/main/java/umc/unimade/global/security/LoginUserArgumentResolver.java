package umc.unimade.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.service.AccountsQueryService;

@Component
@RequiredArgsConstructor
@Transactional
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AccountsQueryService accountsQueryService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isBuyer = parameter.hasParameterAnnotation(LoginBuyer.class) &&
                parameter.getParameterType().isAssignableFrom(Buyer.class);
        boolean isSeller = parameter.hasParameterAnnotation(LoginSeller.class) &&
                parameter.getParameterType().isAssignableFrom(Seller.class);
        return isBuyer || isSeller;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((CustomUserDetail) userDetails).getUsername();

        if (parameter.hasParameterAnnotation(LoginBuyer.class)) {
            return accountsQueryService.getBuyerBySocialId(email);
        } else if (parameter.hasParameterAnnotation(LoginSeller.class)) {
            return accountsQueryService.getSellerByEmail(email);
        }
        return null;
    }
}