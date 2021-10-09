package com.mainstream.todo.Repository;

import com.mainstream.todo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
