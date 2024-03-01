package com.peliculas.peliculasBack.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "movies_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeliculaLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo_operacion;

    @Column(nullable = false)
    private String descripcion_operacion;

    @Column(nullable = false)
    private Date fecha_registro;

    @Column(name = "movie_id") // Nombre de la columna que almacena el ID de la película
    private Long movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Pelicula pelicula;




}
