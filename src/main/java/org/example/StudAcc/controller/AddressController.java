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

    @GetMapping
    public Address byId(@RequestParam("id") int id){
        return service.getById(id);
    }

    @PostMapping("/new")
    public int create(@RequestBody AddressDTO dto){
        return service.create(service.mapToModel(dto));
    }

    @PutMapping("/update")
    public HttpStatus update(@RequestParam("id") int id, @RequestBody AddressDTO dto){
        service.update(service.mapToModel(dto), id);
        return HttpStatus.ACCEPTED;
    }

}
