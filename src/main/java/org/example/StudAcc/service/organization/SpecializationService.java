package org.example.StudAcc.service.organization;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.model.organization.Organization;
import org.example.StudAcc.model.organization.Specialization;
import org.example.StudAcc.repository.organization.OrganizationRepository;
import org.example.StudAcc.repository.organization.SpecializationRepository;
import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.example.StudAcc.utils.exceptions.organization.SpecializationNotFoundException;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpecializationService {

    private final SpecializationRepository repository;

    public List<Specialization> getAll(){
        return repository.findAll();
    }

    public Specialization getById(int id){
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new SpecializationNotFoundException("Специальность с идентификатором " + id + " не найдена.");
        }
    }

    public Specialization getByName(String name) {
        try {
            return repository.findByName(name).get();
        } catch (NoSuchElementException e){
            throw new SpecializationNotFoundException("Специальность " + name + " не найдена.");
        }
    }

    @Transactional
    public void create(String name){
        repository.save(new Specialization(name, Collections.emptyList(), Collections.emptyList()));
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }
}
