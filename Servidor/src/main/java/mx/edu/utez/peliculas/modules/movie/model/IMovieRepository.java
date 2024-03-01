package mx.edu.utez.peliculas.modules.movie.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findAllByTitleContainingIgnoreCaseAndDirectorContainingIgnoreCaseAndCategory_IdAndPublishDateBetween(
            String title, String director, Long category_id, LocalDate firstDate, LocalDate secondDate, Pageable pageable);
    Page<Movie> findAllByTitleContainingIgnoreCaseAndDirectorContainingIgnoreCaseAndCategory_Id(
            String title, String director, Long category_id, Pageable pageable);
    Page<Movie> findAllByTitleContainingIgnoreCaseAndDirectorContainingIgnoreCaseAndPublishDateBetween(
            String title, String director, LocalDate firstDate, LocalDate secondDate, Pageable pageable);
    Page<Movie> findAllByTitleContainingIgnoreCaseAndDirectorContainingIgnoreCase(
            String title, String director, Pageable pageable);
    Page<Movie> findAll(Pageable pageable);

    Boolean existsByTitleContainingIgnoreCase(String title);

    Boolean existsByTitleContainingIgnoreCaseAndIdNot(String title, Long id);

    List<Movie> findAllByCategory_Id(Long categoryId);

}
