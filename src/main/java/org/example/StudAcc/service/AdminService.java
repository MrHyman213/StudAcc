package org.example.StudAcc.service;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.acc.RegistrationUserDto;
import org.example.StudAcc.service.security.RoleService;
import org.example.StudAcc.service.security.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final RoleService roleService;
    private final UserService userService;

    @Transactional
    public void registrationUser(RegistrationUserDto user){
        userService.createNewUser(user);
    }

    @Transactional
    public void addUserOnBlackList(int id){
        userService.findById(id).getRoles().add(roleService.getBlocked());
    }


}
