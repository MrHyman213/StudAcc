package org.example.StudAcc.service.organization;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.model.organization.Group;
import org.example.StudAcc.model.organization.Specialization;
import org.example.StudAcc.repository.organization.GroupRepository;
import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.example.StudAcc.utils.exceptions.organization.GroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository repository;

    public List<Group> getAll(){
        return repository.findAll();
    }

    public Group getByName(String name) {
        try {
            return repository.findByName(name).get();
        } catch (NoSuchElementException e) {
            throw new GroupNotFoundException("Группа " + name + " не найдена.");
        }
    }

    public Group getById(int id){
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new GroupNotFoundException("Группа с идентификатором: " + id + " не найдена.");
        }
    }

    @Transactional
    public void create(String name, Specialization specialization){
        specialization.getGroupList().add(repository.save(new Group(name, specialization)));
    }

    @Transactional
    public void update(Group group, int id){
        group.setId(id);
        repository.save(group);
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }
}
