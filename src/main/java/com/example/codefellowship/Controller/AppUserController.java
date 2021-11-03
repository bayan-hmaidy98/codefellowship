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
        List<Post> list = postRepo.findAllByUserId(user.getId());
        model.addAttribute("users", user);
        model.addAttribute("posts", list);
        return "profile";
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

    @GetMapping("/users/{id}")
    public String getUser(Principal p,Model m, @PathVariable long id){
        try {
            String username = p.getName();
            AppUser currentUser = appUserRepo.findByUserName(username);
            AppUser user = appUserRepo.findById((int) id).get();
            System.out.println(currentUser.getUsers());
            boolean isFollowed = currentUser.isFollowedUser(user);
            boolean isSameUser = false;
            if(username.equals(user.getUsername()))
                isSameUser = true;
            m.addAttribute("user", user);
            m.addAttribute("isSameUser", isSameUser);
            m.addAttribute("username", username);
            m.addAttribute("isFollowed", isFollowed);
            return "users";
        }
        catch(Exception e){
            return "users";
        }
    }

    @PostMapping("/followUser/{username}")
    public RedirectView followUser(Principal p,@PathVariable String username){
        AppUser currentUser = appUserRepo.findByUserName(p.getName());
        AppUser userWantedToFollow = appUserRepo.findByUserName(username);
        currentUser.addFollowToUser(userWantedToFollow);
        appUserRepo.save(currentUser);
        return new RedirectView("/users/"+userWantedToFollow.getId());
    }
    @PostMapping("/unfollowUser/{username}")
    public RedirectView unFollow(Principal p,@PathVariable String username){
        AppUser currentUser = appUserRepo.findByUserName(p.getName());
        AppUser userWantedToUnFollow = appUserRepo.findByUserName(username);
        currentUser.unFollowUser(userWantedToUnFollow);
        appUserRepo.save(currentUser);
        return new RedirectView("/users/"+userWantedToUnFollow.getId());
    }
}



