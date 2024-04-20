package org.example.StudAcc.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.analysis.function.Add;
import org.example.StudAcc.DTO.AddressDTO;
import org.example.StudAcc.DTO.DirectoryDTO;
import org.example.StudAcc.model.address.Address;
import org.example.StudAcc.model.address.District;
import org.example.StudAcc.model.address.Region;
import org.example.StudAcc.service.address.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService service;

    @GetMapping("/oblast/list")
    public List<Region> regionList() {
        return service.getRegionList();
    }

    @GetMapping("/oblast/districts")
    public List<District> districtListByOblast(@RequestParam("id")int id){
        return service.getRegionById(id).getDistrictList();
    }

    @PostMapping("/oblast/new")
    public HttpStatus newOblast(@RequestBody DirectoryDTO dto) {
        service.createRegion(dto.getName());
        return HttpStatus.CREATED;
    }

    @PutMapping("/oblast/{id}")
    public HttpStatus updateOblast(@PathVariable("id")int id, @RequestBody DirectoryDTO dto){
        service.updateRegion(id, dto.getName(), dto.getTypeId());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/oblast/{id}")
    public HttpStatus deleteOblast(@PathVariable("id")int id){
        service.deleteRegion(id);
        return HttpStatus.OK;
    }

    @PostMapping("/district/new")
    public HttpStatus newDistrict(@RequestBody DirectoryDTO dto){
        service.createDistrict(dto.getName(), dto.getParentId());
        return HttpStatus.ACCEPTED;
    }

    @PutMapping("/district/{id}")
    public HttpStatus updateDistrict(@PathVariable("id")int id, @RequestBody DirectoryDTO dto){
        service.updateDistrict(dto.getName(), id, dto.getTypeId());
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/district/{id}")
    public HttpStatus deleteDistrict(@PathVariable("id") int id){
        service.deleteDistrict(id);
        return HttpStatus.OK;
    }


    @GetMapping
    public Address byId(@RequestParam("id") int id){
        return service.getById(id);
    }

    @PostMapping("/new")
    public HttpStatus create(@RequestBody AddressDTO dto){
        service.create(service.mapToModel(dto));
        return HttpStatus.CREATED;
    }

    @PutMapping("/update")
    public HttpStatus update(@RequestParam("id") int id, @RequestBody AddressDTO dto){
        System.out.println(dto);
        service.update(service.mapToModel(dto), id);
        return HttpStatus.ACCEPTED;
    }

}
