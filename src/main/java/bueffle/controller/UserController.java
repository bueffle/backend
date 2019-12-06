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

    @PostMapping("/user")
    public String addUser(@RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        } else {
            userService.addUser(user);
            return "redirect:/welcome";
        }
    }


    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable Long userId) {
        return userService.getUser(userId).getUsername();
    }

}
