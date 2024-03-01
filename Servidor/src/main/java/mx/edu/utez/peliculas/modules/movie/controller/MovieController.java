package mx.edu.utez.peliculas.modules.movie.controller;

import lombok.AllArgsConstructor;
import mx.edu.utez.peliculas.kernel.ResponseApi;
import mx.edu.utez.peliculas.modules.movie.controller.dto.SearchMovieDto;
import mx.edu.utez.peliculas.modules.movie.model.Movie;
import mx.edu.utez.peliculas.modules.movie.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/movie")
@CrossOrigin(origins = {"*"})
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/paged/")
    public ResponseEntity<ResponseApi<Page<Movie>>> getAll(Pageable pageable,
                                                           @RequestBody(required = false) SearchMovieDto searchMovieDto) {
        ResponseApi<Page<Movie>> moviesResponseApi = this.movieService.findAll(pageable, searchMovieDto);
        return new ResponseEntity<>(moviesResponseApi, moviesResponseApi.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<Movie>> getOne(@PathVariable("id") Long id) {
        ResponseApi<Movie> movieResponseApi = this.movieService.findOne(id);
        return new ResponseEntity<>(movieResponseApi, movieResponseApi.getStatus());
    }

    @PostMapping("/")
    public ResponseEntity<ResponseApi<Movie>> save(@RequestBody Movie movie) {
        ResponseApi<Movie> movieResponseApi = this.movieService.save(movie);
        return new ResponseEntity<>(movieResponseApi, movieResponseApi.getStatus());
    }

    @PutMapping("/")
    public ResponseEntity<ResponseApi<Movie>> update(@RequestBody Movie movie) {
        ResponseApi<Movie> movieResponseApi = this.movieService.update(movie);
        return new ResponseEntity<>(movieResponseApi, movieResponseApi.getStatus());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseApi<Movie>> changeStatus(@PathVariable("id") Long id) {
        ResponseApi<Movie> movieResponseApi = this.movieService.changeStatus(id);
        return new ResponseEntity<>(movieResponseApi, movieResponseApi.getStatus());
    }
}
