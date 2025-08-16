package com.bankingapplication.account_service.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationUtil {

    private PaginationUtil() {
        // Private constructor to prevent instantiation
    }

    public static Pageable createPageable(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }

    public static <T> Map<String, Object> createPageResponse(Page<T> page) {
        Map<String, Object> response = new HashMap<>();

        response.put("content", page.getContent());
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("size", page.getSize());
        response.put("hasNext", page.hasNext());
        response.put("hasPrevious", page.hasPrevious());
        response.put("first", page.isFirst());
        response.put("last", page.isLast());

        return response;
    }

    public static <T> Map<String, Object> createListResponse(List<T> items, int totalItems, int page, int size) {
        Map<String, Object> response = new HashMap<>();

        response.put("content", items);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", (int) Math.ceil((double) totalItems / size));
        response.put("size", size);

        return response;
    }
}
