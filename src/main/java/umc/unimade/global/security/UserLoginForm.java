package umc.unimade.global.security;

import umc.unimade.domain.accounts.entity.Role;

public interface UserLoginForm {
    public String getId();
    public Role getRole();
}
