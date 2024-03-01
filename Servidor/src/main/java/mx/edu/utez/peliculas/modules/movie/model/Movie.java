package mx.edu.utez.peliculas.modules.movie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import mx.edu.utez.peliculas.modules.categoy.model.Category;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "VARCHAR(200)", nullable = false)
    private String director;

    @Column(columnDefinition = "DATE", nullable = false)
    LocalDate publishDate;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(columnDefinition = "TINYINT DEFAULT 1", nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"movies"})
    private Category category;
}
