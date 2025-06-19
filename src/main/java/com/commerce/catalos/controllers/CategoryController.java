package com.commerce.catalos.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
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
        return new ResponseEntity<CreateCategoryResponse>(categoryService.createCategory(createCategoryRequest));
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('CAT:NN')")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable final String id, @RequestBody final @Valid UpdateCategoryRequest updateCategoryRequest) {
        Logger.info("3da6c518-963f-4ecb-8d02-573c2f750b08",
                "Received request for updating category : {}", id);
        return new ResponseEntity<UpdateCategoryResponse>(categoryService.updateCategory(id, updateCategoryRequest));
    }
}
