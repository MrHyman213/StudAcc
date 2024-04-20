package org.example.StudAcc.service.address;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.model.address.Region;
import org.example.StudAcc.repository.address.RegionRepository;
import org.example.StudAcc.utils.exceptions.address.OblastNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository repository;

    public List<Region> getAll(){
        try {
            return repository.findAll();
        } catch (NullPointerException e) {
            return Collections.emptyList();
        }
    }

    public Region getById(int id){
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new OblastNotFoundException("Область с идентификатором " + id + " не найдена.");
        }
    }

    @Transactional
    public void create(String name){
        repository.save(new Region(name));
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }
}
