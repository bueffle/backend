package bueffle.controller;

import bueffle.auth.UserValidator;
import bueffle.db.entity.User;
import bueffle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    /**
     * Returns the currently logged in user or an error when not logged in.
     * @return String the current user or an error.
     */
    @GetMapping("/user")
    public String getUser() {
        return userService.findLoggedInUsername();
    }

    /**
     * Method for creating new User. The input gets validated, if the validation ist successful it redirects
     * the user to the starting page.
     * @param user The User which should be created
     * @param bindingResult The result of the validation
     * @return String redirects to the appropriate site.
     */
    @PostMapping("/user")
    public String addUser(@RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        } else {
            userService.addUser(user);
            return "redirect:/";
        }
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

}
