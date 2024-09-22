package com.resul.userservice.service;

import com.resul.userservice.auth.JwtService;
import com.resul.userservice.dto.AuthenticationResponseDTO;
import com.resul.userservice.dto.CreateUserDto;
import com.resul.userservice.dto.UpdateUserDto;
import com.resul.userservice.dto.UserDto;
import com.resul.userservice.entity.UserEntity;
import com.resul.userservice.entity.UserRole;
import com.resul.userservice.exception.UserNotFoundException;
import com.resul.userservice.exception.UsernameAlreadyExistException;
import com.resul.userservice.mapper.UserMapper;
import com.resul.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public List<UserDto> findAll() {
        var userEntities = userRepository.findAll();
        return userMapper.toUserDtoList(userEntities);
    }

    public UserDto findById(Long id) {
        var user = getUser(id);
        return userMapper.toUserDto(user);
    }

    public AuthenticationResponseDTO create(CreateUserDto createUserDto) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(createUserDto.getUsername());
        if (userOptional.isPresent()) {
            throw new UsernameAlreadyExistException("Username already exist: " + createUserDto.getUsername());
        }

        var userEntity = userMapper.toUserEntity(createUserDto);
        userEntity.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        userEntity.setRole(UserRole.USER);
        try {
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException("User registration failed in User Service", e);
        }
        var jwtToken = jwtService.generateToken(userEntity);

        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    public void update(Long id, UpdateUserDto updateUserDto) {
        var userEntity = getUser(id);
        userMapper.updateUserEntity(updateUserDto, userEntity);
        userRepository.save(userEntity);
    }

    private UserEntity getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));
    }

    public void delete(Long id) {
        var userEntity = getUser(id);
        userRepository.delete(userEntity);
    }

    public UserDto findByUsername(String username) {
        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return userMapper.toUserDto(userEntity);
    }
}