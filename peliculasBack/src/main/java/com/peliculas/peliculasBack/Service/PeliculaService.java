package com.peliculas.peliculasBack.Service;

import com.peliculas.peliculasBack.Dto.PeliculaDto;
import com.peliculas.peliculasBack.Models.Pelicula;
import com.peliculas.peliculasBack.Models.PeliculaLogsRepository;
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

    @Autowired
    private PeliculaLogsRepository peliculaLogsRepository;

    @Transactional(readOnly = true)
    public CustomResponse<List<Pelicula>> getAll(){
        return new CustomResponse<>(
                this.repository.findAll(),
                false,
                200,
                "OK"
        );
    }

    public CustomResponse<Pelicula> insert(Pelicula movies) {
        Pelicula savedMovie = this.repository.saveAndFlush(movies);

        // inserta un registro en la tabla de bitácora
        peliculaLogsRepository.insertMovieLog(savedMovie.getId());

        return new CustomResponse<>(
                savedMovie,
                false,
                200,
                "Película registrada"
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

    //delete by id
    public CustomResponse<Boolean> deleteById(Long id) {
        if (!this.repository.existsById(id)) {
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "La película no existe"
            );
        }

        peliculaLogsRepository.disableForeignKeyChecks();
        peliculaLogsRepository.insertDeleteLog(id);
        this.repository.deleteById(id);
        peliculaLogsRepository.enableForeignKeyChecks();

        return new CustomResponse<>(
                true,
                false,
                200,
                "Película eliminada exitosamente"
        );
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
