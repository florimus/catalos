package com.commerce.catalos.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.categories.CategoryResponse;
import com.commerce.catalos.models.categories.CategoryStatusUpdateResponse;
import com.commerce.catalos.models.categories.CreateCategoryRequest;
import com.commerce.catalos.models.categories.CreateCategoryResponse;
import com.commerce.catalos.models.categories.UpdateCategoryRequest;
import com.commerce.catalos.models.categories.UpdateCategoryResponse;
import com.commerce.catalos.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    @PreAuthorize("hasRole('CAT:NN')")
    public ResponseEntity<CreateCategoryResponse> createCategory(
            @RequestBody final @Valid CreateCategoryRequest createCategoryRequest) {
        Logger.info("3da6c518-963f-4ecb-8d02-573c2f750b08",
                "Received request for creating new category : {}", createCategoryRequest.getName());
        return new ResponseEntity<CreateCategoryResponse>(
                categoryService.createCategory(createCategoryRequest));
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('CAT:NN')")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable final String id,
            @RequestBody final @Valid UpdateCategoryRequest updateCategoryRequest) {
        Logger.info("3da6c518-963f-4ecb-8d02-573c2f750b08",
                "Received request for updating category : {}", id);
        return new ResponseEntity<UpdateCategoryResponse>(
                categoryService.updateCategory(id, updateCategoryRequest));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('CAT:LS')")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable final String id) {
        Logger.info("a7cd4cbb-5afa-446e-8d64-adf921347a23",
                "Received request for fetch category : {}", id);
        return new ResponseEntity<CategoryResponse>(categoryService.getCategory(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('CAT:LS')")
    public ResponseEntity<Page<CategoryResponse>> listCategories(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = SortConstants.ALL) String parent,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("a5851c0e-1b0d-4a69-8a53-eed00b62fd68",
                "Received request to list the categories with query: {} and parent: {}",
                query, parent);
        return new ResponseEntity<Page<CategoryResponse>>(
                categoryService.listCategories(query, parent, pageable));
    }

    @PreAuthorize("hasRole('CAT:NN')")
    @PatchMapping("/id/{id}/status/{status}")
    public ResponseEntity<CategoryStatusUpdateResponse> updateCategoryStatus(
            @PathVariable final String id, @PathVariable final boolean status) {
        Logger.info("3d4e4cf3-285c-4e4c-b7f3-c81d702d4514",
                "Received request for update category status with id: {}",
                id);
        return new ResponseEntity<CategoryStatusUpdateResponse>(
                categoryService.updateCategoryStatus(id, status));
    }
}
