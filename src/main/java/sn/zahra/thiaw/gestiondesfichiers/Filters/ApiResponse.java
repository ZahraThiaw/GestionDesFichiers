package sn.zahra.thiaw.gestiondesfichiers.Filters;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private String status;
    private int statusCode;

    // Pagination metadata
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data, List<String> errors, String status, int statusCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.status = status;
        this.statusCode = statusCode;
    }

}
