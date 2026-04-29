package com.example.final_project.service;


import com.example.final_project.dto.MovieDTO;
import com.example.final_project.entity.Movie;
import com.example.final_project.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieDTO> getAllMovies(Long userId) {
        return movieRepository.findByUserId(userId)
                .stream()
                .map(
                        movie -> new MovieDTO(
                                movie.getTitle(),
                                movie.getGenre(),
                                movie.getReleaseYear(),
                                movie.getIsWatched(),
                                movie.getRating()
                        )
                )
                .collect(Collectors.toList());
    }
}
