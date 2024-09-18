package com.resul.userservice.mapper;

import com.resul.userservice.entity.UserEntity;
import com.resul.userservice.dto.CreateUserDto;
import com.resul.userservice.dto.UpdateUserDto;
import com.resul.userservice.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    List<UserDto> toUserDtoList(List<UserEntity> userEntities);
    UserDto toUserDto(UserEntity userEntity);
    UserEntity toUserEntity(CreateUserDto createUserDto);
    void updateUserEntity(UpdateUserDto updateUserDto, @MappingTarget UserEntity userEntity);
}