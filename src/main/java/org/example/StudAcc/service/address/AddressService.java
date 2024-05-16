package org.example.StudAcc.service.address;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.AddressDTO;
import org.example.StudAcc.model.address.*;
import org.example.StudAcc.repository.address.AddressRepository;
import org.example.StudAcc.service.ListService;
import org.example.StudAcc.utils.exceptions.address.AddressNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository repository;
    private final ListService listService;
    private final ModelMapper mapper;

    public Address mapToModel(AddressDTO dto){
        Address address = mapper.map(dto, Address.class);
        address.setDistrict(listService.getDistrictById(dto.getIdDistrict()));
        return address;
    }

    public Address getById(int id){
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new AddressNotFoundException("Адрес с идентификатором " + id + " не найден.");
        }
    }

    @Transactional
    public int create(Address address){
        return repository.save(address).getId();
    }

    @Transactional
    public void update(Address address, int id){
        address.setId(id);
        repository.save(address);
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }

}
