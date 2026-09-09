package com.neuroforge.controller;

import com.neuroforge.entity.User;
import com.neuroforge.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins="*")
public class UserController { 
    private final UserRepository r;

    public UserController(UserRepository r) {
        this.r = r;
    }

    @GetMapping 
    public List<User> all() {
        return r.findAll();
    } 

    @GetMapping("/{id}") 
    public User get(@PathVariable Long id) {
        return r.findById(id).orElseThrow();
    }

    @PostMapping 
    public ResponseEntity<?> create(@RequestBody User u) {
        if (u.getEmail() != null && r.findAll().stream().anyMatch(existing -> u.getEmail().equalsIgnoreCase(existing.getEmail()))) {
            return ResponseEntity.badRequest().body("Email '" + u.getEmail() + "' is already registered! Please use a unique email.");
        }
        User saved = r.save(u);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}") 
    public User update(@PathVariable Long id, @RequestBody User u) {
        u.setId(id);
        return r.save(u);
    } 

    @DeleteMapping("/{id}") 
    public void delete(@PathVariable Long id) {
        r.deleteById(id);
    }
}
