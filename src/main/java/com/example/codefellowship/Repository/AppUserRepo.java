package com.example.codefellowship.Repository;

import com.example.codefellowship.Models.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepo extends CrudRepository <AppUser, Integer> {
    AppUser findByUserName(String userName);

}
