package org.example.StudAcc.controller.acc;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.acc.JwtRequest;
import org.example.StudAcc.DTO.acc.RegistrationUserDto;
import org.example.StudAcc.service.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return service.createAuthToken(authRequest);
    }


}
