package com.peliculas.peliculasBack.Controller;

import com.peliculas.peliculasBack.Dto.PeliculaDto;
import com.peliculas.peliculasBack.Models.Pelicula;
import com.peliculas.peliculasBack.Service.CustomResponse;
import com.peliculas.peliculasBack.Service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-movieBack")
@CrossOrigin(origins = "*")
public class PeliculaController {
    @Autowired
    PeliculaService service;

    @GetMapping("/")
    public ResponseEntity<List<Pelicula>> getAll(){
        return new ResponseEntity(
                this.service.getAll(),
                HttpStatus.OK
        );
    }

    @PostMapping("/findFilter/")
    public ResponseEntity<CustomResponse<List<Pelicula>>> findByFilter(@Valid @RequestBody PeliculaDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        CustomResponse<List<Pelicula>> moviesFilter = this.service.findDirectorGenero(dto);
        return new ResponseEntity<>(moviesFilter, HttpStatus.CREATED);
    }

    @PostMapping("/findRangeDates/")
    public ResponseEntity<CustomResponse<List<Pelicula>>> findByRangeDates(@Valid @RequestBody PeliculaDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        CustomResponse<List<Pelicula>> moviesFilter = this.service.findRangeDates(dto);
        return new ResponseEntity<>(moviesFilter, HttpStatus.CREATED);
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse<Pelicula>> insert(@Valid @RequestBody PeliculaDto dto){
        return new ResponseEntity<>(
                this.service.insert(dto.getPelicula()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<CustomResponse<Pelicula>> update(
            @RequestBody PeliculaDto dto, @Valid BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                this.service.update(dto.getPelicula()),
                HttpStatus.CREATED
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<Boolean>> deleteMovieById(@PathVariable Long id) {
        CustomResponse<Boolean> response = service.deleteById(id);
        if (response.getError()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
