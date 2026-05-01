package com.example.final_project.service;

import com.example.final_project.dto.MovieDTO;
import com.example.final_project.entity.Movie;
import com.example.final_project.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // GET all movies that belong to a user.
    public List<MovieDTO> getAllMovies(Long userId) {
        return movieRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // GET a movie by the ID
    public MovieDTO getMovieById(Long id, Long userId){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie with id " + id + " not found"));

        if(!movie.getUserId().equals(userId)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have permission to view this movie");
        }
        return toDTO(movie);
    }

    // POST create a new movie
    public MovieDTO createMovie(MovieDTO movieDTO, Long userId){
        Movie movie = new Movie();
        movie.setUserId(userId);
        movie.setTitle(movieDTO.getTitle());
        movie.setGenre(movieDTO.getGenre());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setRating(movieDTO.getRating());
        movie.setIsWatched(movieDTO.getIsWatched() != null ? movieDTO.getIsWatched() : false);

        Movie saved = movieRepository.save(movie);
        return toDTO(saved);
    }

    // PUT update an existing movie
    public MovieDTO updateMovie(MovieDTO movieDTO, Long id, Long userId){
        // Look for the book in the DB
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie with id " + id + " not found"));

        // Check if the movie belongs to the user
        if(!movie.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have permission to view this movie");
        }

        movie.setTitle(movieDTO.getTitle());
        movie.setGenre(movieDTO.getGenre());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setIsWatched(movieDTO.getIsWatched() != null ? movieDTO.getIsWatched() : movie.getIsWatched());
        movie.setRating(movieDTO.getRating());

        Movie updated = movieRepository.save(movie);
        return toDTO(updated);
    }

    // DELETE a movie
    public void deleteMovie(Long id, Long userId){
        // Look for the book in the DB
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie with id " + id + " not found"));

        // Check if the movie belongs to the user
        if(!movie.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have permission to view this movie");
        }

        movieRepository.delete(movie);
    }

    // Helper function
    private MovieDTO toDTO(Movie movie){
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setIsWatched(movie.getIsWatched());
        dto.setRating(movie.getRating());
        return dto;
    }
}
