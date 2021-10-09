package com.mainstream.todo.Controller;

import com.mainstream.todo.Service.ServiceImple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final ServiceImple serviceImple;

    @Autowired
    public UserController(ServiceImple serviceImple) {
        this.serviceImple = serviceImple;
    }

}
