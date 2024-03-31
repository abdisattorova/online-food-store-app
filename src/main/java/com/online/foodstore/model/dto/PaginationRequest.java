package com.online.foodstore.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {
    private int page;

    private int size = 10;

    private String search;

    private LocalDate fromDate;

    private LocalDate toDate;

    public int getPage() {
        return Math.max(page, 0);
    }

    public Pageable getPageRequest() {
        return PageRequest.of(getPage(), getSize());
    }

}
