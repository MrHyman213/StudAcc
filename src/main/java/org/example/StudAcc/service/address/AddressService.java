package org.example.StudAcc.service.address;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.AddressDTO;
import org.example.StudAcc.model.address.*;
import org.example.StudAcc.repository.address.AddressRepository;
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
    private final RegionService oblastService;
    private final DistrictService districtService;

    private final ModelMapper mapper;

    // Default methods
    // Области
    ////////////////////////////////////////////////////////////////////
    public List<Region> getRegionList(){
        return oblastService.getAll();
    }

    @Transactional
    public void createRegion(String name) {
        oblastService.create(name);
    }

    @Transactional
    public void updateRegion(int id, String name, int typeId){
        oblastService.getById(id).setName(name);
    }

    @Transactional
    public void deleteRegion(int id){
        oblastService.delete(id);
    }

    public Region getRegionById(int id){
        return oblastService.getById(id);
    }

    // Районы
    ////////////////////////////////////////////////////////////////////
    public District getDistrictById(int id){
        return districtService.getById(id);
    }

    @Transactional
    public void createDistrict(String name, int parentId) {
        districtService.create(name, getRegionById(parentId));
    }

    @Transactional
    public void updateDistrict(String name, int id, int typeId){
        getDistrictById(id).setName(name);
    }

    @Transactional
    public void deleteDistrict(int id){
        District district = getDistrictById(id);
        district.getRegion().getDistrictList().remove(district);
        districtService.delete(id);
    }

    // Address
    public Address mapToModel(AddressDTO dto){
        Address address = mapper.map(dto, Address.class);
        address.setDistrict(getDistrictById(dto.getIdDistrict()));
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
    public void create(Address address){
        repository.save(address);
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
