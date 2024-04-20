package org.example.StudAcc.service.security;

import lombok.RequiredArgsConstructor;
import org.example.StudAcc.DTO.acc.JwtRequest;
import org.example.StudAcc.DTO.acc.JwtResponse;
import org.example.StudAcc.DTO.acc.RegistrationUserDto;
import org.example.StudAcc.utils.BodyError;
import org.example.StudAcc.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager manager;
    private final JwtUtils utils;

    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new BodyError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getName());
        String token = utils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new BodyError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
