package com.peliculas.peliculasBack.Service;

import com.peliculas.peliculasBack.Dto.PeliculaDto;
import com.peliculas.peliculasBack.Models.Pelicula;
import com.peliculas.peliculasBack.Models.PeliculaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class PeliculaService {
    @Autowired
    private PeliculaRepository repository;

    @Transactional(readOnly = true)
    public CustomResponse<List<Pelicula>> getAll(){
        return new CustomResponse<>(
                this.repository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Pelicula> insert(Pelicula movie){
        Optional<Pelicula> exists =this.repository.findByName(movie.getName());
        if(exists.isPresent()){
            return new CustomResponse<>(null,true,400,"El nombre de la peli esta registrado");
        }
        return new CustomResponse<>(
                this.repository.saveAndFlush(movie),
                false,
                200,
                "Película Registrado!"
        );
    }

    //Validar que existe el id
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Pelicula> update(Pelicula peli){
        if(!this.repository.existsById(peli.getId())){
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "La peli no existe"
            );
        }
        return new CustomResponse<>(
                this.repository.saveAndFlush(peli),
                false,
                200,
                "Pelicula actualizada!"
        );
    }

    private static final Logger logger = LoggerFactory.getLogger(PeliculaService.class);
    @Transactional
    public CustomResponse<Pelicula> delete(Long id) {
        try {
            Optional<Pelicula> peliculaOptional = repository.findById(id);

            if (!peliculaOptional.isPresent()) {
                return new CustomResponse<>(
                        null,
                        true,
                        400,
                        "La película no existe"
                );
            }

            repository.deleteById(id);
            return new CustomResponse<>(
                    null,
                    false,
                    200,
                    "Pelicula eliminada!"
            );
        } catch (Exception e) {
            e.printStackTrace(); // Loguea la excepción para diagnóstico
            return new CustomResponse<>(
                    null,
                    true,
                    500,
                    "Error interno al intentar eliminar la película"
            );
        }
    }


    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<List<Pelicula>> findDirectorGenero(PeliculaDto dto) {
        List<Pelicula> movie;
        System.out.println("Service"+dto.getGenero()+ dto.getDirector()+dto.getName());
        movie = this.repository.findByGeneroContainingOrDirectorContainingOrNameContaining(dto.getGenero(), dto.getDirector(),dto.getName());
        if (movie.isEmpty()) {
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "No se encontraron películas con el género y director especificados"
            );
        }

        return new CustomResponse<>(
                movie,
                false,
                200,
                "OK"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<List<Pelicula>> findRangeDates(PeliculaDto dto) {
        List<Pelicula> movie;
        movie = this.repository.findByPublishDateBetween(dto.getFirstDate(),dto.getTwoDate());
        if (movie.isEmpty()) {
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "No se encontraron películas con el rengo de fechas especificados"
            );
        }
        return new CustomResponse<>(
                movie,
                false,
                200,
                "OK"
        );
    }








}
