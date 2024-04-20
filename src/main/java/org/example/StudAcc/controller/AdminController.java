package org.example.StudAcc.controller;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.acc.RegistrationUserDto;
import org.example.StudAcc.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService service;

    @PostMapping("/registration")
    public HttpStatus createNewUser(@RequestBody RegistrationUserDto dto){
        service.registrationUser(dto);
        return HttpStatus.CREATED;
    }

    @PostMapping("/block")
    public HttpStatus blockUser(@RequestParam("userId")int id){
        service.addUserOnBlackList(id);
        return HttpStatus.OK;
    }
}
