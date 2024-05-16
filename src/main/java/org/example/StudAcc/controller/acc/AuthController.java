package org.example.StudAcc.controller.acc;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.acc.JwtRequest;
import org.example.StudAcc.model.acc.Role;
import org.example.StudAcc.service.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return service.createAuthToken(authRequest);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_BLOCKED"})
    @GetMapping("/info")
    public List<Role> getRoles(Principal principal){
        System.out.println("Username - " + principal.getName());
        return service.getRolesByUsername(principal.getName());
    }

}
