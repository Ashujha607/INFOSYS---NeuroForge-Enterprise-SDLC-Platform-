package com.neuroforge.controller;
import com.neuroforge.entity.BugReport;
 import com.neuroforge.repository.BugReportRepository; 
 import org.springframework.web.bind.annotation.*;
  import java.util.List;
@RestController @RequestMapping("/api/bugs") @CrossOrigin(origins="*")
public class BugReportController 
{ private final BugReportRepository r; 
    public BugReportController(BugReportRepository r){this.r=r;}
@GetMapping public List<BugReport> all(){return r.findAll();}
 @GetMapping("/{id}") public BugReport get(@PathVariable Long id){return r.findById(id).orElseThrow();}
@PostMapping public BugReport create(@RequestBody BugReport b){return r.save(b);}
@PutMapping("/{id}") public BugReport update(@PathVariable Long id,
    @RequestBody BugReport b){b.setId(id);return r.save(b);} 
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id)
    {r.deleteById(id);}}
