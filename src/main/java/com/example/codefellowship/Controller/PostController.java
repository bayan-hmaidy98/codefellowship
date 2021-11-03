package com.example.codefellowship.Controller;

import com.example.codefellowship.Models.AppUser;
import com.example.codefellowship.Models.Post;
import com.example.codefellowship.Repository.AppUserRepo;
import com.example.codefellowship.Repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PostController {

    @Autowired
    PostRepo postRepo;
    @Autowired
    AppUserRepo applicationUserRepository;
    @GetMapping("/profile")
    public String getPosts(Model m, Principal p){
        String username = p.getName();
        AppUser user = applicationUserRepository.findByUserName(username);
        long id = user.getId();
        Optional<List<Post>> posts = Optional.ofNullable(postRepo.findAllByUserId(id));

        m.addAttribute("posts",posts);
        m.addAttribute("username",p.getName());
        return "profile";
    }

    @GetMapping("/feed")
    public String showFeed(Model m, Principal p){
        String username = p.getName();
        AppUser user = applicationUserRepository.findByUserName(username);
        List<Long> ids = user.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList());
        List<Optional<List<Post>>> postsOfUsers = new ArrayList<>();
        for (Long i : ids) {
            Optional<List<Post>> allByUserId = Optional.ofNullable(postRepo.findAllByUserId(i));
            postsOfUsers.add(allByUserId);
        }
        System.out.println(postsOfUsers);
        m.addAttribute("postsOfUsers",postsOfUsers);
        m.addAttribute("username",p.getName());
        return "feed";
    }

    @PostMapping("/addPost")
    public RedirectView createPost(Principal p, String body){
        String username = p.getName();
        AppUser user = applicationUserRepository.findByUserName(username);
        Post newPost = new Post(body,user);
        postRepo.save(newPost);

        return new RedirectView("/profile");
    }

    @GetMapping("/addPost")
    public String getAddPost(){
        return "addPost";
    }


}