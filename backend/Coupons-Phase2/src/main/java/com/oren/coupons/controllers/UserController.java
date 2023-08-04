package com.oren.coupons.controllers;


import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.dto.User;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.logic.UserLogic;
import com.oren.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserLogic userLogic;

    @Autowired
    public UserController(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    @PostMapping()
    public void createUser(@RequestBody User user) throws ApplicationException {
        userLogic.addUser(user);
    }


    @PutMapping()
    public void updateUser(@RequestHeader("Authorization") String token, @RequestBody User user) throws Exception {

        userLogic.updateUser(user, token);
    }

    @GetMapping()
    public List<User> getAllUsers(@RequestHeader("Authorization") String token) throws ApplicationException {
        return userLogic.getAllUsers(token);
    }

    @GetMapping("/{userId}")
    public User getUser(@RequestHeader("Authorization") String token,@PathVariable("userId") int userId) throws ApplicationException {
        return userLogic.getUser(userId, token);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@RequestHeader("Authorization") String token,@PathVariable("userId") int userId) throws ApplicationException {
        userLogic.deleteUser(userId, token);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws ApplicationException {
        //todo: remove print before production
        System.out.println("\n\n\"user: "+ user.getUsername()+" ---> pass: "+user.getPassword()+"\n\n");
        String token=userLogic.login(user.getUsername(), user.getPassword());


        return token;
    }
}