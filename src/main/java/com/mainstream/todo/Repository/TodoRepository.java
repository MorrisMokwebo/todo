package com.mainstream.todo.Repository;

import com.mainstream.todo.Model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
