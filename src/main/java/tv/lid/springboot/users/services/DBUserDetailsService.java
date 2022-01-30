package tv.lid.springboot.users.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DBUserDetailsService implements UserDetailsService {
    // сервис доступа к пользователям
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
        // пытаемся найти пользователя по логину
        final tv.lid.springboot.users.entities.User user = this.userService.findByLoginOrEmail(loginOrEmail);
        if (user == null) {
            throw new UsernameNotFoundException("Не удалось найти заданного пользователя!");
        }

        // создаём пустые права доступа
        final List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));

        // возвращаем пользователя
        return new User(loginOrEmail, user.getPwdHash(), authorities);
    }
}
