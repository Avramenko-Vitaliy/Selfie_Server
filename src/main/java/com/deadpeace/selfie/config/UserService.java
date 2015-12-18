package com.deadpeace.selfie.config;

import com.deadpeace.selfie.model.User;
import com.deadpeace.selfie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Виталий on 06.10.2015.
 */
@Service("userService")
public class UserService implements UserDetailsService
{
    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user=repository.findByUsername(username);
        if(user!=null)
            return user;
        else
            throw new UsernameNotFoundException("User not found!");
    }
}