package bueffle.controller;

import bueffle.auth.PasswordValidator;
import bueffle.auth.UserNameValidator;
import bueffle.db.entity.User;
import bueffle.exception.UserNotFoundException;
import bueffle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserNameValidator userNameValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    /**
     * Returns the currently logged in user or an error when not logged in.
     * @return String the current user or an error.
     */
    @GetMapping("/user")
    public User getUser() {
        User result = userService.findByUsername(userService.findLoggedInUsername()).orElseThrow(UserNotFoundException::new);
        result.emptyRestrictedFields();
        return result;
    }

    /**
     * Return the name of an user for a provided User Id.
     * @param userId the Id for the user who's name should be provided.
     * @return String the name of the User with the provided Id.
     */
    @GetMapping("/user/{userId}")
    public String getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId).getUsername();
    }

    /**
     * Method for creating new User. The input gets validated, if the validation ist successful it redirects
     * the user to the starting page.
     * @param user The User which should be created
     * @param bindingResult The result of the validation
     * @return String redirects to the appropriate site.
     */
    @PostMapping("/user")
    public User addUser(@RequestBody User user, BindingResult bindingResult) {
        userNameValidator.validate(user, bindingResult);
        passwordValidator.validate(user, bindingResult);
        if (!bindingResult.hasErrors()) {
            userService.addUser(user);
            user.emptyRestrictedFields();
            return user;
        }
        else {
            return new User();
        }
    }

    /**
     * Method for updating a user. (username or password). If the PUT Request just contains an updated username,
     * it will validate the username and update it. If the PUT Request contains a new password and its confirmation,
     * it will validate and update the password.
     * @param user The User which should be update (self)
     * @param bindingResult The result of the validation
     * @return String redirects to the appropriate site.
     */
    @PutMapping("/user")
    public String updateUser(@RequestBody User user, BindingResult bindingResult) {
        // Case 1: Password should be validated and updated
        if (!user.hasUsername()) {
            user.setUsername(userService.findLoggedInUsername());
            passwordValidator.validate(user, bindingResult);
            if (!bindingResult.hasErrors()) {
                userService.updatePassword(user);
                return "redirect:/profile";
            }
        }
        // Case 2: username should be validated and updated
        else {
            userNameValidator.validate(user, bindingResult);
            if (!bindingResult.hasErrors()) {
                userService.updateUsername(user);
                return "redirect:/profile";
            }
        }
        return "profile/edit";
    }
}
