package je.techtribes.component.social;

import com.structurizr.annotation.Component;
import je.techtribes.util.AbstractComponent;
import je.techtribes.util.SecurityRoles;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.LinkedList;
import java.util.List;

@Component(description = "Allows users to sign-in with their Twitter ID.")
public class TwitterSignInComponent extends AbstractComponent implements SignInAdapter {

    private String twitterIdForAdministration;

    public void setTwitterIdForAdministration(String twitterIdForAdministration) {
        this.twitterIdForAdministration = twitterIdForAdministration;
    }

    public String signIn(String username, Connection<?> connection, NativeWebRequest request) {
        List<GrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(SecurityRoles.USER));

        if (username.equals(twitterIdForAdministration)) {
            authorities.add(new SimpleGrantedAuthority(SecurityRoles.ADMIN));
        }

        User user = new User(username, "", authorities);
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(result);

        return "/user/profile";
    }

}