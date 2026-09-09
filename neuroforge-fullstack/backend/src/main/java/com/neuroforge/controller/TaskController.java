package com.neuroforge.controller;
import com.neuroforge.entity.Task; 
import com.neuroforge.repository.TaskRepository; 
import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/tasks") @CrossOrigin(origins="*")
public class TaskController { private final TaskRepository r; 
    public TaskController(TaskRepository r){this.r=r;}
@GetMapping public List<Task> all(){return r.findAll();} 
@GetMapping("/{id}") public Task get(@PathVariable Long id){return r.findById(id).orElseThrow();}
@PostMapping public Task create(@RequestBody Task t){return r.save(t);} @PutMapping("/{id}") public Task update(@PathVariable Long id,@RequestBody Task t){t.setId(id);return r.save(t);} @DeleteMapping("/{id}") public void delete(@PathVariable Long id){r.deleteById(id);}}
