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
        if(!userRoleExists("ROLE_USER")) {
            addUserRole("ROLE_USER");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }

    /**
     * Checks if a role with name "rolename" exists
     * @param rolename the name to check
     * @return boolean if the name to checks exists
     */
    private boolean userRoleExists(String rolename) {
        return roleRepository.findByName(rolename) != null;
    }

    /**
     * Adds a user role with name "rolename"
     * @param rolename the name to add
     */
    private void addUserRole(String rolename) { roleRepository.save(new Role(rolename)); }

    /**
     * Returns a user by its userId
     * @param userId the id of the user to return
     * @return User with id UserId
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Returns a Optional User Object for better Nullpointer Handling
     * @param username the name to find
     * @return if present, a user object.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds the logged in username by querying the SecurityContextHolder and returns it as a string. Returns "not
     * logged in" as String when there is no logged in user. This can be changed if needed.
     * @return String the name of the logged in user
     */
    public String findLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return  ((UserDetails)principal).getUsername();
        }
        else {
            return principal.toString();
        }
    }

    /**
     * Updates a username.
     * @param updatedUser the updated User Object containing the new name.
     * @return User the updated user
     */
    public User updateUsername(User updatedUser) {
        User oldUser = userRepository.findByUsername(findLoggedInUsername()).orElseThrow(UserNotFoundException::new);
        oldUser.setUsername(updatedUser.getUsername());
        userRepository.save(oldUser);
        oldUser.emptyRestrictedFields();
        return oldUser;
    }

    /**
     * Updates a password.
     * @param updatedUser the updated User Object containing the new password and passwordConfirmation.
     * @return User the updated user
     */
    public User updatePassword(User updatedUser) {
        User oldUser = userRepository.findByUsername(findLoggedInUsername()).orElseThrow(UserNotFoundException::new);
        oldUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userRepository.save(oldUser);
        oldUser.emptyRestrictedFields();
        return oldUser;
    }
}
