package online.muydinov.securedoc.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import online.muydinov.securedoc.dto.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@Setter
public class TokenData {
    private User user;
//    private Claims claims;
    private boolean valid;
    private List<GrantedAuthority> authorities;
}
