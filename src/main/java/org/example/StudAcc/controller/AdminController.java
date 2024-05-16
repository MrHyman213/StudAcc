package org.example.StudAcc.controller;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.acc.RegistrationUserDto;
import org.example.StudAcc.model.acc.Role;
import org.example.StudAcc.model.acc.User;
import org.example.StudAcc.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService service;

    @GetMapping("/info")
    public List<Role> getRoles(@RequestParam("username") String username){
        return service.getRolesByUsername(username);
    }

    @GetMapping("/list")
    public List<User> getList(){
        return service.getUserList();
    }

    @PostMapping("/registration")
    public HttpStatus createNewUser(@RequestBody RegistrationUserDto dto) {
        service.registrationUser(dto);
        return HttpStatus.CREATED;
    }

    @PostMapping("/block")
    public HttpStatus blockUser(@RequestParam("userId")int id) {
        service.addUserOnBlackList(id);
        return HttpStatus.OK;
    }

    @PutMapping("/update")
    public HttpStatus updateUser(@RequestParam("id")int id, @RequestBody RegistrationUserDto dto) {
        service.updateUser(dto, id);
        return HttpStatus.ACCEPTED;
    }
}
