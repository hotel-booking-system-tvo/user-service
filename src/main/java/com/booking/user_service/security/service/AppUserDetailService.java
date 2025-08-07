package com.booking.user_service.security.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.booking.user_service.entity.User;
import com.booking.user_service.repository.UserRepository;
import com.booking.user_service.security.jwt.AppUserDetail;

@Service
public class AppUserDetailService  implements UserDetailsService{
    private static final Logger LOGGER = LogManager.getLogger(AppUserDetailService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<User> userEntity = userRepository.findByUsername((id));
        if(userEntity.isEmpty())
        {
            throw new UsernameNotFoundException("User not found");
        }
        return AppUserDetail.build(userEntity.get());
    }
}
