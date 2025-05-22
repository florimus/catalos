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

    private long totalHitsCount;

    private int currentPage;

    private int pageSize;

    private int totalPages;

    private boolean hasNext;

    private boolean hasPrevious;

    public Page(List<T> hits, long totalHitsCount, int currentPage, int pageSize) {
        this.hits = hits;
        this.totalHitsCount = totalHitsCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;

        this.totalPages = pageSize > 0
                ? (int) Math.ceil((double) totalHitsCount / pageSize)
                : 0;

        this.hasNext = (this.currentPage + 1) < this.totalPages;
        this.hasPrevious = this.currentPage > 0;
    }

}
