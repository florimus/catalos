package com.commerce.catalos.core.configurations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> {

    private List<T> hits;

    private Integer totalHitsCount;

    private Integer currentPage;

    private Integer pageSize;

    private Integer totalPages;

    private boolean hasNext;

    private boolean hasPrevious;

    public Page(List<T> hits, Integer totalHitsCount, Integer currentPage, Integer pageSize) {
        this.hits = hits;
        this.totalHitsCount = totalHitsCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;

        this.totalPages = (pageSize != null && pageSize > 0)
                ? (int) Math.ceil((double) totalHitsCount / pageSize)
                : 0;

        this.hasNext = this.currentPage < this.totalPages;
        this.hasPrevious = this.currentPage > 1;
    }

}
