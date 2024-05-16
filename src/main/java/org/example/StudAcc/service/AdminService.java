package org.example.StudAcc.service;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.acc.RegistrationUserDto;
import org.example.StudAcc.model.acc.Role;
import org.example.StudAcc.model.acc.User;
import org.example.StudAcc.service.security.RoleService;
import org.example.StudAcc.service.security.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final RoleService roleService;
    private final UserService userService;
    private final ModelMapper mapper;

    @Transactional
    public void registrationUser(RegistrationUserDto user){
        userService.createNewUser(user);
    }

    @Transactional
    public void addUserOnBlackList(int id){
        userService.findById(id).getRoles().add(roleService.getBlocked());
    }

    @Transactional
    public void updateUser(RegistrationUserDto dto, int id) {
        User user = mapper.map(dto, User.class);
        user.setRoles(dto.getRoleList().stream().map(roleService::getById).collect(Collectors.toList()));
        userService.updateUser(user, id);
    }

    public List<Role> getRolesByUsername(String username){
        return userService.getInfoByUsername(username);
    }

    public List<User> getUserList() {
        return userService.getList();
    }
}
