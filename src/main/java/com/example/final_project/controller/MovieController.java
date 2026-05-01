package com.example.final_project.controller;


import com.example.final_project.dto.MovieDTO;
import com.example.final_project.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    /// no to sure why this is needed but, it is for the knowing who the authenticated user is for data isolation
    private Long getUserId()
    {
        return (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movieService.createMovie(movieDTO, getUserId()));
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies()
    {
        return ResponseEntity.ok(movieService.getAllMovies(getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id)
    {
        return ResponseEntity.ok(movieService.getMovieById(id, getUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @RequestBody MovieDTO movieDTO)
    {
        return ResponseEntity.ok(movieService.updateMovie(movieDTO, id, getUserId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id)
    {
        movieService.deleteMovie(id, getUserId());
        return ResponseEntity.noContent().build();
    }
}
