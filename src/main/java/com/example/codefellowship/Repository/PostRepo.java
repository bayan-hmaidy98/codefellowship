package com.example.codefellowship.Repository;

import com.example.codefellowship.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    Optional< List<Post> > findAllByUser_Username(String username);
    Optional< List<Post> > findAllByUserId(Long id);
}