package com.neuroforge.controller;
import com.neuroforge.entity.Project; import com.neuroforge.repository.ProjectRepository; 
import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/projects") @CrossOrigin(origins="*")
public class ProjectController { private final ProjectRepository r;
     public ProjectController(ProjectRepository r){this.r=r;}
@GetMapping public List<Project> all(){return r.findAll();} 
@GetMapping("/{id}") public Project get(@PathVariable Long id){return r.findById(id).orElseThrow();}
@PostMapping public Project create(@RequestBody Project p){return r.save(p);} 
@PutMapping("/{id}") public Project update(@PathVariable Long id,@RequestBody Project p)
{p.setId(id);return r.save(p);} @DeleteMapping("/{id}") public void delete(@PathVariable Long id)
{r.deleteById(id);}}
