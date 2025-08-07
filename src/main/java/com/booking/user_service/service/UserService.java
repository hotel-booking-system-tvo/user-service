package com.booking.user_service.service;

import com.booking.user_service.dto.UserDto;

public interface UserService {
    void register(UserDto userDto);
    UserDto getByUsername(String username);
}
