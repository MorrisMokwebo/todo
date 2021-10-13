package com.mainstream.todo.Controller;

import com.mainstream.todo.Model.Todo;
import com.mainstream.todo.Model.User;
import com.mainstream.todo.Service.ServiceImple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final ServiceImple serviceImple;

    @Autowired
    public UserController(ServiceImple serviceImple) {
        this.serviceImple = serviceImple;
    }

    /**
     * Get all users resources
     *
     * @return
     */
    public List<User> getAllUsers() {
        return serviceImple.retrieveAllUser();
    }

    /**
     * Get user by id resource
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return serviceImple.retrieveUserById(id);
    }

    /**
     * Delete user by id resource
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        serviceImple.deleteUser(id);
    }

    /**
     * Create new user resource
     *
     * @param user
     * @return
     */
    @PostMapping("/newuser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return serviceImple.createUser(user);
    }

    /**
     * Update user resource
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") long id, @RequestBody User user) {
        return serviceImple.updateUserById(id, user);
    }


    /**
     * Get all user todo resource
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/todo")
    public List<Todo> getUserTodo(@PathVariable("id") long id) {
        return serviceImple.retrieveUserTodos(id);
    }

    /**
     * Create new user todo resource
     *
     * @param id
     * @param todo
     * @return
     */
    @PostMapping("/{id}/todo")
    public ResponseEntity<Todo> createUserTodo(@PathVariable("id") long id, @RequestBody Todo todo) {
        return serviceImple.createNewUserTodo(id, todo);
    }

    /**
     * Update existing user todo resource
     *
     * @param userId
     * @param todoId
     * @param todo
     * @return
     */
    @PutMapping("/{userid}/{todoid}/todo")
    public Todo updateUserTodo(@PathVariable("userid") long userId, @PathVariable("todoid") long todoId, @RequestBody Todo todo) {
        return serviceImple.updateUserTodo(userId, todoId, todo);
    }
}
