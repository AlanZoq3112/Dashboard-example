package com.peliculas.peliculasBack.Dto;

import com.peliculas.peliculasBack.Models.Pelicula;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeliculaDto {
    private Long id;
    @NotEmpty(message = "Campo obligatorio")
    @Size(min = 1, max = 100)
    private String name;
    private String genero;
    private String description;
    private String director;
    private LocalDate firstDate;
    private LocalDate twoDate;
    private LocalDate publishDate;

    public Pelicula getPelicula(){
        return new Pelicula(
                getId(),
                getName(),
                getGenero(),
                getDescription(),
                getDirector(),
                getPublishDate()
        );
    }



}
