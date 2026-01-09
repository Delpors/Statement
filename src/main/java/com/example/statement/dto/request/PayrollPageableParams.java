package com.example.statement.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class PayrollPageableParams {
        String sortBy = "totalIssued";
        String direction = "asc";
        int page = 0;
        int size = 50;

    public Pageable getPageable() {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return PageRequest.of(page, size, sort);
    }
}
