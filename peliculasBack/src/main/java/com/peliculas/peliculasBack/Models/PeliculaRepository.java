package com.peliculas.peliculasBack.Models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula,Long> {
    Optional<Pelicula> findByName(String Name);
    List<Pelicula> findByGeneroContainingOrDirectorContainingOrNameContaining(String Genero, String Director,String Name);

    List<Pelicula> findByPublishDateBetween(
            LocalDate firstDate,LocalDate secondDate
    );

}
