package org.example.StudAcc.service.student;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.model.student.OrderNumber;
import org.example.StudAcc.repository.student.OrderNumberRepository;
import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.example.StudAcc.utils.exceptions.student.EducationNotFoundException;
import org.example.StudAcc.utils.exceptions.student.OrderNumberNotFoundException;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderNumberService {

    private final OrderNumberRepository repository;

    public List<OrderNumber> getAll(){
        return repository.findAll();
    }

    public OrderNumber getById(int id){
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new OrderNumberNotFoundException("Приказ с номером " + id + " не найден.");
        }
    }

    public OrderNumber getByName(String name){
        try {
            return repository.findByName(name).get();
        } catch (NoSuchElementException e){
            throw new OrderNumberNotFoundException("Приказ " + name + " не найден.");
        }
    }

    @Transactional
    public OrderNumber create(String name){
        return repository.save(new OrderNumber(name, Collections.emptyList()));
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }
}
