package com.rakib.blog.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {

    private List<T> content;
    private PageMeta page;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageMeta {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean lastPage;
    }

    public static <T> PaginatedResponse<T> of(List<T> content, int pageNumber, int pageSize,
                                                long totalElements, int totalPages, boolean lastPage) {
        PageMeta meta = new PageMeta(pageNumber, pageSize, totalElements, totalPages, lastPage);
        return new PaginatedResponse<>(content, meta);
    }
}
