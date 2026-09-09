package com.neuroforge.repository;
import com.neuroforge.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskRepository extends JpaRepository<Task, Long> {}
