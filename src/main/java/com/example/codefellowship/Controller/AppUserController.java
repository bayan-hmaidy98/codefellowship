package com.example.codefellowship.Controller;

import com.example.codefellowship.Models.AppUser;
import com.example.codefellowship.Models.Post;
import com.example.codefellowship.Repository.AppUserRepo;
import com.example.codefellowship.Repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;


@Controller
public class AppUserController {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    PostRepo postRepo;

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

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserRepo.findByUserName(userDetails.getUsername());
        List<Post> list = postRepo.findAllByUserId(user.getId()).orElseThrow();
        model.addAttribute("users", user);
        model.addAttribute("posts", list);
        return "profile";
    }

    @GetMapping("/posts")
    public String getPosts(@ModelAttribute Post posts, Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Post> post = postRepo.findAllByUser_Username(userDetails.getUsername()).orElseThrow();
        model.addAttribute("posts", post);

        return "post";
    }

    @PostMapping("/posts")
    public RedirectView addPosts(@ModelAttribute Post posts) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserRepo.findByUserName(userDetails.getUsername());
        posts.setUser(user);
        appUserRepo.save(user);
        postRepo.save(posts);
        user.setPosts(Collections.singletonList(posts));
        return new RedirectView("/profile") ;
    }

    @GetMapping("/profile/{id}")
    public String getProfilePageById(@PathVariable String id , Model model) {
        long Id = Long.parseLong(id);
        AppUser user = appUserRepo.findAppUserById(Id);
        List<Post> list = postRepo.findAllByUserId(user.getId()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("posts", list);
        return "oneUser";
    }
}



