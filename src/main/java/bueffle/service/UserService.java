package bueffle.service;

import bueffle.auth.BackendUserDetails;
import bueffle.db.entity.Role;
import bueffle.db.entity.User;
import bueffle.exception.UserNotFoundException;
import bueffle.model.RoleRepository;
import bueffle.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return user.map(BackendUserDetails::new).get();
    }

    /**
     * Adding user, adding a role for the user if it has none yet.
     * Hashing the Password so it doesn't get saved in plain text in the database.
     * Sending the user to the userRepository.
     * @param user The User to be added.
     */
    public void addUser(User user) {
        if(!userRoleExists()) {
            addUserRole();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }

    private boolean userRoleExists() {
        return roleRepository.findByName("ROLE_USER") != null;
    }

    private void addUserRole() {
        roleRepository.save(new Role("ROLE_USER"));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String findLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return  ((UserDetails)principal).getUsername();
        }
        else {
            return "Not logged in";
        }
    }

}
