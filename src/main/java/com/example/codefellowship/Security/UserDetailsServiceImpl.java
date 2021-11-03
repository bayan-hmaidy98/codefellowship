package com.example.codefellowship.Security;

import com.example.codefellowship.Models.AppUser;
import com.example.codefellowship.Repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByUserName(username);

        if(appUser == null){
            throw new UsernameNotFoundException(username + "Not Found");
        }
        return appUser;
    }
    }

