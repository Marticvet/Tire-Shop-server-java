package com.tireshop.tiresShop.service.security;

// import com.tireshop.tiresShop.service.model.Role;
import com.tireshop.tiresShop.service.model.UserEntity;
import com.tireshop.tiresShop.service.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.security.config.core.GrantedAuthorityDefaults;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// import java.util.Collection;
// import java.util.List;
// import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new User(user.getUsername(), user.getPassword(), null);
    }

    // private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles)
    // {
    // return roles.stream().map(role -> new
    // SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    // }
}
