package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.CategoryHelper;
import com.commerce.catalos.models.categories.CategoryResponse;
import com.commerce.catalos.models.categories.CategoryStatusUpdateResponse;
import com.commerce.catalos.models.categories.CreateCategoryRequest;
import com.commerce.catalos.models.categories.CreateCategoryResponse;
import com.commerce.catalos.models.categories.UpdateCategoryRequest;
import com.commerce.catalos.models.categories.UpdateCategoryResponse;
import com.commerce.catalos.persistence.dtos.Category;
import com.commerce.catalos.persistence.repositories.CategoryRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final AuthContext authContext;

    private Category getCategoryById(final String id) {
        return categoryRepository.findCategoryByIdAndEnabled(id, true);
    }

    @Override
    public CreateCategoryResponse createCategory(final CreateCategoryRequest createCategoryRequest) {
        Category parentCategory = new Category();

        if (createCategoryRequest.getParentId() != null && !createCategoryRequest.getParentId().isBlank()) {
            Logger.info("3eaadb1e-6abc-40cc-8b45-ebcf016609aa", "Finding parent category id: {}",
                    createCategoryRequest.getParentId());
            parentCategory = getCategoryById(createCategoryRequest.getParentId());
            if (parentCategory.getId() == null) {
                Logger.error("b1832042-e63a-475f-a9e5-37dc5bad2e15", "Parent category not found");
                throw new NotFoundException("Parent category not found");
            }
        }

        Logger.info("̦607360db-536c-4260-9d59-47d8eb18eec5", "Parent Category is: {}", parentCategory.getName());
        Category category = CategoryHelper.toCategoryFromCreateCategoryRequest(createCategoryRequest, parentCategory);
        category.setActive(true);
        category.setEnabled(true);
        category.setCreatedAt(new Date());
        category.setCreatedBy(authContext.getCurrentUser().getEmail());
        category = categoryRepository.save(category);

        Logger.info("6787a804-5018-4dd7-aa68-9ce0db1d097e", "Category created with id: {}", category.getId());
        return CategoryHelper.toCreateCategoryResponseFromCategory(category);
    }

    @Override
    public UpdateCategoryResponse updateCategory(final String id, final UpdateCategoryRequest updateCategoryRequest) {
        Category category = getCategoryById(id);
        if (null == category) {
            Logger.error("74684318-4438-4956-8b62-b50b3b923cb0", "Category not found with id: {}", id);
            throw new NotFoundException("Category not found");
        }

        if (updateCategoryRequest.getParentId() != null && !updateCategoryRequest.getParentId().isBlank()) {
            Logger.info("f7f80e2d-e4b9-4e4c-8f5d-df58bbf02a49", "Finding parent category id: {}",
                    updateCategoryRequest.getParentId());
            Category parentCategory = getCategoryById(updateCategoryRequest.getParentId());
            parentCategory = getCategoryById(updateCategoryRequest.getParentId());

            if (parentCategory.getId() == null) {
                Logger.error("b1832042-e63a-475f-a9e5-37dc5bad2e15", "Parent category not found");
                throw new NotFoundException("Parent category not found");
            }

            if (parentCategory.getId() == category.getId()) {
                Logger.error("c14c974c-7494-40b0-b970-a2b39ca13c82", "Parent category cannot be itself");
                throw new ConflictException("Parent category cannot be itself");
            }
            category.setParentName(parentCategory.getName());
            category.setParentId(parentCategory.getId());
        }

        if (updateCategoryRequest.getName() != null || !updateCategoryRequest.getName().isBlank()) {
            category.setName(updateCategoryRequest.getName());
        }
        if (updateCategoryRequest.getSeoTitle() != null || !updateCategoryRequest.getSeoTitle().isBlank()) {
            category.setSeoTitle(updateCategoryRequest.getSeoTitle());
        }
        if (updateCategoryRequest.getSeoDescription() != null || !updateCategoryRequest.getSeoDescription().isBlank()) {
            category.setSeoDescription(updateCategoryRequest.getSeoDescription());
        }
        Logger.info("0cc20e57-d915-459a-a13c-3cea72c76ffe", "Category updated with id: {}", category.getId());
        return CategoryHelper.toUpdateCategoryResponseFromCategory(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getCategory(final String id) {
        Category category = this.getCategoryById(id);
        if (category == null) {
            Logger.error("c6361f8e-7bcb-489e-a6ad-cbcdda700bb7", "Category not found");
            throw new NotFoundException("Category not found");
        }
        Logger.info("db646900-e073-4567-aac7-660dedb8d42f", "Category found with id: {}", category.getId());
        return CategoryHelper.toCategoryResponseFromCategory(category);
    }

    @Override
    public Page<CategoryResponse> listCategories(final String query, final String parent, Pageable pageable) {
        Logger.info("5541cdbc-2b41-40c0-a142-2f583f781872",
                "Finding the categories with query: {}, parent: {} and pagination: {}",
                query, parent, pageable);
        Page<Category> categories = categoryRepository.searchCategories(query, parent, pageable);
        return new Page<CategoryResponse>(
                CategoryHelper.toCategoryResponseFromCategories(categories.getHits()),
                categories.getTotalHitsCount(),
                categories.getCurrentPage(),
                categories.getPageSize());
    }

    @Override
    public CategoryStatusUpdateResponse updateCategoryStatus(String id, boolean status) {
        if (id.isBlank()) {
            Logger.error("d71553fd-13c3-468b-a178-dae172c5c865", "Category id is mandatory");
            throw new BadRequestException("Invalid category id");
        }
        Category category = this.getCategoryById(id);
        if (category == null) {
            Logger.error("b7eeff0c-ecba-4090-ad87-39d855ead4d6", "Category not found");
            throw new NotFoundException("Category not found");
        }
        category.setActive(status);
        category.setUpdatedAt(new Date());
        category.setUpdatedBy(authContext.getCurrentUser().getEmail());
        category = categoryRepository.save(category);
        Logger.info("09bce99f-b267-40dc-bfd8-58de58e4635a", "Category updated with id: {}", category.getId());
        return CategoryStatusUpdateResponse.builder()
                .status(category.isActive())
                .message(category.isActive() ? "Category Activated" : "Category Deactivated")
                .build();
    }

}
