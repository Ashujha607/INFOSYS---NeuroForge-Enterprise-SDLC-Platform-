package com.neuroforge.service;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public class CrudService<T> {
    private final JpaRepository<T, Long> repository;
    public CrudService(JpaRepository<T, Long> repository){ this.repository=repository; }
    public List<T> all(){ return repository.findAll(); }
    public T get(Long id){ return repository.findById(id).orElseThrow(); }
    public T save(T value){ return repository.save(value); }
    public void delete(Long id){ repository.deleteById(id); }
}
