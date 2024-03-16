package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.Repository.UserRepository;
import io.Adrestus.Backend.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class JwtUserDetailsService {
    @Autowired
    @Lazy
    private UserRepository userDao;

    public UserDetails loadUserDetails(String username) throws UsernameNotFoundException {
        UserModel user = userDao.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found with username: " + username);
        return new User(user.getUsername(), user.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
