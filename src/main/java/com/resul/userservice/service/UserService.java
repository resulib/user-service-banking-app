package com.resul.userservice.service;

import com.resul.userservice.dto.CreateUserDto;
import com.resul.userservice.dto.UpdateUserDto;
import com.resul.userservice.dto.UserDto;
import com.resul.userservice.entity.UserEntity;
import com.resul.userservice.exception.UserNotFoundException;
import com.resul.userservice.mapper.UserMapper;
import com.resul.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> findAll() {
        var userEntities = userRepository.findAll();
        return userMapper.toUserDtoList(userEntities);
    }

    public UserDto findById(Long id) {
        var user = getUser(id);
        return userMapper.toUserDto(user);
    }

    public void create(CreateUserDto createUserDto) {
        var userEntity = userMapper.toUserEntity(createUserDto);
        userRepository.save(userEntity);
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
}