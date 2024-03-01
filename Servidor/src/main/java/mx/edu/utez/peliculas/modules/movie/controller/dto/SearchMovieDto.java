package mx.edu.utez.peliculas.modules.movie.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchMovieDto {
    private String title;
    private String director;
    private Long categoryId;
    private LocalDate firstDate;
    private LocalDate secondDate;
}
