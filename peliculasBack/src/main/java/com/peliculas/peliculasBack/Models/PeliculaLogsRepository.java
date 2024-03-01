package com.peliculas.peliculasBack.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PeliculaLogsRepository extends JpaRepository<Pelicula, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO movies_logs (tipo_operacion, descripcion_operacion, fecha_registro, movie_id) " +
            "VALUES ('INSERT', 'Nueva pelicula insertada', NOW(), :movieId)", nativeQuery = true)
    void insertMovieLog(Long movieId);

    @Transactional
    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS=0", nativeQuery = true)
    void disableForeignKeyChecks();

    @Transactional
    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS=1", nativeQuery = true)
    void enableForeignKeyChecks();


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO movies_logs (tipo_operacion, descripcion_operacion, fecha_registro, movie_id) " +
            "VALUES ('ELIMINACION', CONCAT('Película eliminada: ', (SELECT name FROM movies WHERE id = :movieId)), NOW(), :movieId)",
            nativeQuery = true)
    void insertDeleteLog(Long movieId);


}

