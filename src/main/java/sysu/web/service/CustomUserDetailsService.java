package sysu.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import sysu.database.repository.UserRepository;
import sysu.web.bean.User;
import sysu.web.security.UserSecurity;


public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(userName);
		
		if(user==null) {
			throw new UsernameNotFoundException("UserName:"+userName + "not found.");
		}
		// UserSecurity实现UserDetails
        UserSecurity userSecurity = new UserSecurity(user);
        return userSecurity; 
	}

}
