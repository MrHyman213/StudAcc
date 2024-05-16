package org.example.StudAcc.controller;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.OrganizationDTO;
import org.example.StudAcc.model.organization.Organization;
import org.example.StudAcc.service.organization.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/org")
public class OrganizationController {

    private final OrganizationService service;

    @GetMapping("/info")
    public Organization info() {
        return service.getInfo();
    }

    @PutMapping("/update")
    public HttpStatus organization(@RequestBody OrganizationDTO dto) {
        service.update(service.mapToModel(dto));
        return HttpStatus.ACCEPTED;
    }

    @PutMapping("/director")
    public HttpStatus director(@RequestBody int id) {
        service.updateDirector(id);
        return HttpStatus.ACCEPTED;
    }

    @PutMapping("/secretary")
    public HttpStatus secretary(@RequestBody int id) {
        service.updateSecretary(id);
        return HttpStatus.ACCEPTED;
    }

}
