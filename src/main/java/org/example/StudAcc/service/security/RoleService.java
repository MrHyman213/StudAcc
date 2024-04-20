package org.example.StudAcc.service.security;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.model.acc.Role;
import org.example.StudAcc.repository.acc.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository repository;

    public Role getUserRole(){
        return repository.findByName("ROLE_USER").get();
    }

    public Role getAdminRole(){
        return repository.findByName("ROLE_ADMIN").get();
    }

    public Role getBlocked(){
        return repository.findByName("ROLE_BLOCKED").get();
    }
}
