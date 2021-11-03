package com.example.codefellowship.Controller;

import com.example.codefellowship.Models.AppUser;
import com.example.codefellowship.Repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;



@Controller
public class AppUserController {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AppUserRepo appUserRepo;

    @GetMapping("/")
    public String getHome(Principal p,Model m){
        if(p != null)
            m.addAttribute("username",p.getName());
        return ("index");
    }

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String userName,
                               @RequestParam String password,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String bio,
                               @RequestParam String dateOfBirth){
        AppUser appUser = new AppUser(userName, encoder.encode(password), firstName, lastName, bio, dateOfBirth);
        appUserRepo.save(appUser);
        return "login";
    }

    @GetMapping("login")
    public String getLogIn(){
        return "login";
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable(value = "id") Integer id, Principal p, Model m){
        if(p != null){
            System.out.println("bebo");
            AppUser applicationUser = appUserRepo.findById(id).get();
            m.addAttribute("user", applicationUser);
            return "profile";
        }
        return "/error?message=You%are%not%allow%to%delete%the%user";
    }


}
