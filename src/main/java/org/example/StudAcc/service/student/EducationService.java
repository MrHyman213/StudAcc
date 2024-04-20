package org.example.StudAcc.service.student;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.example.StudAcc.model.student.Education;
import org.example.StudAcc.repository.student.EducationRepository;
import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.example.StudAcc.utils.exceptions.student.EducationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationService {

    private final EducationRepository repository;

    public List<Education> getAll(){
        return repository.findAll();
    }

    public Education getById(int id){
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new EducationNotFoundException("Образование с идентификатором " + id + " не найден.");
        }
    }

    public Education getByName(String name){
        try {
            return repository.findByName(name).get();
        } catch (NoSuchElementException e){
            throw new EducationNotFoundException("Образование " + name + " не найден.");
        }
    }

    @Transactional
    public Education create(String name){
        return repository.save(new Education(name));
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }
}
