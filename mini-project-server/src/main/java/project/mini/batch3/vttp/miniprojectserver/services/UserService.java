package project.mini.batch3.vttp.miniprojectserver.services;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.mini.batch3.vttp.miniprojectserver.configs.SecurityConfig;
import project.mini.batch3.vttp.miniprojectserver.exceptions.AppException;
import project.mini.batch3.vttp.miniprojectserver.models.User;
import project.mini.batch3.vttp.miniprojectserver.models.UserDto;
import project.mini.batch3.vttp.miniprojectserver.repositories.UserRepository;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SecurityConfig securityConfig;

    public UserDto login(String username, String password) {
            Optional<List<User>> optUser = userRepo.getUserByUsername(username);

            if(optUser.get().size() == 0) {
                throw new AppException("Username not found", HttpStatus.NOT_FOUND);
            }

            User user = optUser.get().get(0);
            char[] charPw = password.toCharArray();

            if(securityConfig.passwordEncoder().matches(CharBuffer.wrap(charPw), user.getEnc_password())) {
                UserDto userDto = UserDto.builder().id(user.getId()).username(user.getUsername()).build();
                return userDto;
            }
            throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(String username, String password) {
        Optional<List<User>> optUser = userRepo.getUserByUsername(username);

        if(optUser.get().size() != 0) {
            throw new AppException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString().substring(0, 8));
        user.setUsername(username);
        user.setPassword(securityConfig.passwordEncoder().encode(CharBuffer.wrap(password.toCharArray())));

        userRepo.insertNewUser(user);

        UserDto userDto = UserDto.builder().id(user.getId()).username(user.getUsername()).build();

        return userDto;
    }

    public User findByUsername(String username) {
        Optional<List<User>> optUser = userRepo.getUserByUsername(username);

        if(optUser.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        return optUser.get().get(0);
    }
}
