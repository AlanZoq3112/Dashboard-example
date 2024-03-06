package mx.edu.utez.peliculas.modules.movie.controller;

import lombok.AllArgsConstructor;
import mx.edu.utez.peliculas.kernel.ResponseApi;
import mx.edu.utez.peliculas.modules.logs.model.Log;
import mx.edu.utez.peliculas.modules.logs.service.LogService;
import mx.edu.utez.peliculas.modules.movie.controller.dto.SearchMovieDto;
import mx.edu.utez.peliculas.modules.movie.model.Movie;
import mx.edu.utez.peliculas.modules.movie.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/movie")
@CrossOrigin(origins = {"*"})
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final LogService logService;

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
    @GetMapping("/")
    public List<Log> getLogs() {
        return this.logService.getAll();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
            System.out.println(id);
            movieService.delete(id);
            return new ResponseEntity<>("Pelicula eliminada con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la pelicula: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseApi<Movie>> changeStatus(@PathVariable("id") Long id) {
        ResponseApi<Movie> movieResponseApi = this.movieService.changeStatus(id);
        return new ResponseEntity<>(movieResponseApi, movieResponseApi.getStatus());
    }
}
