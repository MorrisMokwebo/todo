package com.mainstream.todo.Service;

import com.mainstream.todo.Exception.TodoNotFoundException;
import com.mainstream.todo.Exception.UserNotFoundException;
import com.mainstream.todo.Model.Status;
import com.mainstream.todo.Model.Todo;
import com.mainstream.todo.Model.User;
import com.mainstream.todo.Repository.TodoRepository;
import com.mainstream.todo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceImple {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Autowired
    public ServiceImple(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }

    //Get all users
    public List<User> retrieveAllUser() {
        return userRepository.findAll();
    }

    //get user by id
    public User retrieveUserById(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("id: " + id);
        }

        User userToBeFetched = optionalUser.get();
        return userToBeFetched;
    }

    //delete user by id
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    // create user
    public ResponseEntity<User> createUser(User newUser) {
        User savedUser = userRepository.save(newUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("id").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    //Update User by id
    public User updateUserById(long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id: " + id);
        }

        User userTobeUpdated = userOptional.get();

        return userRepository.save(userTobeUpdated);
    }


    //retrieve all user todos
    public List<Todo> retrieveUserTodos(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id: " + id);
        }

        return userOptional.get().getTodos();
    }

    //create new todo for a user
    public ResponseEntity<Todo> createNewUserTodo(long id, Todo todo) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id " + id);
        }
        User user = userOptional.get();
        todo.setUser(user);

        todo.setStatus(Status.NEW);
        todoRepository.save(todo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("id").buildAndExpand(todo.getId()).toUri();

        return ResponseEntity.created(location).build();

    }


    //update userTodo by id
    public Todo updateUserTodo(long userId, long todoId, Todo todo) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id: " + userId);
        }
        if (!todoOptional.isPresent()) {
            throw new TodoNotFoundException("id: " + todoId);
        }

        User user = userOptional.get();
        Todo todoTobeUpdated = todoOptional.get();

        //create a condition that wont let the user not to update the todo status to NEW
        //the status need to be in progress or done.


        todoTobeUpdated.setModifiedAt(new Date());
        return todoRepository.save(todoTobeUpdated);
    }

    //delete user to do my id
}
