package com.neuroforge.controller;
import com.neuroforge.entity.Sprint; import com.neuroforge.repository.SprintRepository; 
import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/sprints") @CrossOrigin(origins="*")
public class SprintController { private final SprintRepository r; 
    public SprintController(SprintRepository r){this.r=r;}
@GetMapping public List<Sprint> all(){return r.findAll();} 
@GetMapping("/{id}") public Sprint get(@PathVariable Long id){return r.findById(id).orElseThrow();}
@PostMapping public Sprint create(@RequestBody Sprint s){return r.save(s);} 
@PutMapping("/{id}") public Sprint update(@PathVariable Long id,
 @RequestBody Sprint s){s.setId(id);return r.save(s);}
  @DeleteMapping("/{id}") public void delete(@PathVariable Long id){r.deleteById(id);}}
