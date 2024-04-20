package org.example.StudAcc.service.address;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.model.address.District;
import org.example.StudAcc.model.address.Region;
import org.example.StudAcc.repository.address.DistrictRepository;
import org.example.StudAcc.utils.exceptions.address.DistrictNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DistrictService {

    private final DistrictRepository repository;

    public District getById(int id){
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new DistrictNotFoundException("Район с идентификатором " + id + " не найден.");
        }
    }

    @Transactional
    public void create(String name, Region region){
        region.getDistrictList().add(repository.save(new District(name, region)));
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }
}

