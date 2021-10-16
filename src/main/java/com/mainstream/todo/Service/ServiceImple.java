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

    private  final  EmailSenderService emailSenderService;

    @Autowired
    public ServiceImple(UserRepository userRepository, TodoRepository todoRepository, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.emailSenderService = emailSenderService;
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
      Optional<User> userOptional = this.userRepository.findById(id);
      if(!userOptional.isPresent()){
          throw new UserNotFoundException("id :"+id);
      }

      User userToBeDeleted = userOptional.get();
      userRepository.delete(userToBeDeleted);
    }

    // create user
    public ResponseEntity<User> createUser(User newUser) {
        User savedUser = userRepository.save(newUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("id").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    //Update User by id
    public User updateUserById(long id, User user) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id: " + id);
        }

        User userTobeUpdated = userOptional.get();

        userTobeUpdated.setFirstName(user.getFirstName());
        userTobeUpdated.setLastName(user.getLastName());
        userTobeUpdated.setEmail(user.getEmail());

        this.userRepository.save(userTobeUpdated);
        return  userTobeUpdated;
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
        todo.setModifiedAt(new Date());
        todo.setCreatedAt(new Date());

        emailSenderService.sendEmail(user.getEmail(),todo.getTodo(),"Hello there, this is a notification to let you know that your todo was successfully created.");

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

        todoTobeUpdated.setModifiedAt(new Date());
        todoTobeUpdated.setTodo(todo.getTodo());
        todoTobeUpdated.setDescription(todo.getDescription());

        //create a condition that wont let the user not to update the todo status to NEW
        //the status need to be in progress or done.
        if(todo.getStatus().equals(Status.NEW)){
            throw new RuntimeException("todo id: "+todoId +"Cannot be NEW");
        }
        todoTobeUpdated.setStatus(todo.getStatus());
        return todoRepository.save(todoTobeUpdated);
    }

    //delete user to do my id
    public void deleteUserTodo(long userId, long todoId){
        Optional<User> userOptional = this.userRepository.findById(userId);
        Optional<Todo> todoOptional = this.todoRepository.findById(todoId);

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id: " + userId);
        }
        if (!todoOptional.isPresent()) {
            throw new TodoNotFoundException("id: " + todoId);
        }

        Todo todoToBeDeleted = todoOptional.get();
        todoRepository.delete(todoToBeDeleted);

    }
}
