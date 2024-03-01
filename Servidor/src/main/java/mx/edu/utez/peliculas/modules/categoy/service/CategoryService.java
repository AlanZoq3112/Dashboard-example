package mx.edu.utez.peliculas.modules.categoy.service;

import mx.edu.utez.peliculas.kernel.Errors;
import mx.edu.utez.peliculas.kernel.ResponseApi;
import mx.edu.utez.peliculas.modules.categoy.model.Category;
import mx.edu.utez.peliculas.modules.categoy.model.ICategoryRepository;
import mx.edu.utez.peliculas.modules.movie.model.IMovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    private final ICategoryRepository iCategoryRepository;
    private final IMovieRepository iMovieRepository;

    public CategoryService(ICategoryRepository iCategoryRepository, IMovieRepository iMovieRepository) {
        this.iCategoryRepository = iCategoryRepository;
        this.iMovieRepository = iMovieRepository;
    }

    @Transactional(readOnly = true)
    public ResponseApi<Page<Category>> findAll(Pageable pageable) {
        return new ResponseApi<>(
                this.iCategoryRepository.findAll(pageable),
                HttpStatus.OK,
                false,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public ResponseApi<Category> findOne(Long id) {
        Optional<Category> optionalCategory = this.iCategoryRepository.findById(id);
        return optionalCategory.map(category -> new ResponseApi(
                category,
                HttpStatus.OK,
                false,
                "Ok"
        )).orElseGet(() -> new ResponseApi<>(
                HttpStatus.NOT_FOUND,
                true,
                Errors.NO_DATA_FOUND.name()
        ));
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseApi<Category> save(Category category) {
        if (this.iCategoryRepository.existsByNameContainingIgnoreCase(category.getName())) {
            return new ResponseApi<>(
                    HttpStatus.BAD_REQUEST,
                    true,
                    Errors.DUPLICATED_RECORD.name()
            );
        }
        return new ResponseApi<>(
                this.iCategoryRepository.saveAndFlush(category),
                HttpStatus.CREATED,
                false,
                "Category created"
        );
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseApi<Category> update(Category category) {
        if (!this.iCategoryRepository.existsById(category.getId())) {
            return new ResponseApi<>(
                    HttpStatus.NOT_FOUND,
                    true,
                    Errors.NO_DATA_FOUND.name()
            );
        }
        if (this.iCategoryRepository.existsByNameContainingIgnoreCaseAndIdNot(category.getName(), category.getId())) {
            return new ResponseApi<>(
                    HttpStatus.BAD_REQUEST,
                    true,
                    Errors.DUPLICATED_RECORD.name()
            );
        }
        return new ResponseApi<>(
                this.iCategoryRepository.saveAndFlush(category),
                HttpStatus.OK,
                false,
                "Category updated"
        );
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseApi<Category> changeStatus(Long id) {
        Optional<Category> optionalCategory = this.iCategoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            return new ResponseApi<>(
                    HttpStatus.NOT_FOUND,
                    true,
                    Errors.NO_DATA_FOUND.name()
            );
        }
        if (!this.iMovieRepository.findAllByCategory_Id(id).isEmpty()) {
            return new ResponseApi<>(
                    HttpStatus.BAD_REQUEST,
                    true,
                    Errors.RECORD_HAS_DEPENDENCIES.name()
            );
        }
        Category existingCategory = optionalCategory.get();
        existingCategory.setStatus(!existingCategory.getStatus());
        return new ResponseApi<>(
                this.iCategoryRepository.saveAndFlush(existingCategory),
                HttpStatus.OK,
                false,
                "Category status updated"
        );
    }
}
