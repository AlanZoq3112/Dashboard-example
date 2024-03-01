package mx.edu.utez.peliculas.modules.categoy.controller;

import lombok.AllArgsConstructor;
import mx.edu.utez.peliculas.kernel.ResponseApi;
import mx.edu.utez.peliculas.modules.categoy.model.Category;
import mx.edu.utez.peliculas.modules.categoy.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/category")
@CrossOrigin(origins = {"*"})
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/paged/")
    public ResponseEntity<ResponseApi<Page<Category>>> getAll(Pageable pageable) {
        ResponseApi<Page<Category>> categoriesResponseApi = this.categoryService.findAll(pageable);
        return new ResponseEntity<>(categoriesResponseApi, categoriesResponseApi.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<Category>> getOne(@PathVariable("id") Long id) {
        ResponseApi<Category> categoryResponseApi = this.categoryService.findOne(id);
        return new ResponseEntity<>(categoryResponseApi, categoryResponseApi.getStatus());
    }

    @PostMapping("/")
    public ResponseEntity<ResponseApi<Category>> save(@RequestBody Category category) {
        ResponseApi<Category> categoryResponseApi = this.categoryService.save(category);
        return new ResponseEntity<>(categoryResponseApi, categoryResponseApi.getStatus());
    }

    @PutMapping("/")
    public ResponseEntity<ResponseApi<Category>> update(@RequestBody Category category) {
        ResponseApi<Category> categoryResponseApi = this.categoryService.update(category);
        return new ResponseEntity<>(categoryResponseApi, categoryResponseApi.getStatus());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseApi<Category>> changeStatus(@PathVariable("id") Long id) {
        ResponseApi<Category> categoryResponseApi = this.categoryService.changeStatus(id);
        return new ResponseEntity<>(categoryResponseApi, categoryResponseApi.getStatus());
    }
}
