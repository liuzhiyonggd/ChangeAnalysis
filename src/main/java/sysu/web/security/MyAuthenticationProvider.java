package sysu.web.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import sysu.database.repository.UserRepository;
import sysu.web.bean.User;



@Component
public class MyAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private UserRepository userRepository;
	/**
     * 自定义验证方式
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        User user = userRepository.findByUserName(username);
        if(user == null){
            throw new BadCredentialsException("Username not found.");
        }
        UserSecurity userSecurity = new UserSecurity( userRepository.findByUserName(username));
        

        //加密过程在这里体现
        if (!password.equals(userSecurity.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }

        Collection<? extends GrantedAuthority> authorities = userSecurity.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userSecurity, password, authorities);
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}
