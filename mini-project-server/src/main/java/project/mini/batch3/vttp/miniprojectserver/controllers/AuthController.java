package project.mini.batch3.vttp.miniprojectserver.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import project.mini.batch3.vttp.miniprojectserver.configs.UserAuthenticationProvider;
import project.mini.batch3.vttp.miniprojectserver.models.LoginRequest;
import project.mini.batch3.vttp.miniprojectserver.models.SignUpRequest;
import project.mini.batch3.vttp.miniprojectserver.models.UserDto;
import project.mini.batch3.vttp.miniprojectserver.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userSvc;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid LoginRequest loginRequest) {
        UserDto userDto = userSvc.login(loginRequest.username(), loginRequest.password());
        userDto.setToken(userAuthenticationProvider.createToken(userDto));
        return ResponseEntity.ok(userDto);

    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpRequest user) {
        UserDto createdUser = userSvc.register(user.username(), user.password());
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
}
