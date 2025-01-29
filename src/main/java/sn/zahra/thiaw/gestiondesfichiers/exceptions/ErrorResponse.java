package sn.zahra.thiaw.gestiondesfichiers.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private String details;
    private String path;

    // Constructeur explicite avec tous les arguments
    public ErrorResponse(int status, String message, String details, String path) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.path = path;
    }

    // Constructeur sans arguments (si nécessaire pour certaines utilisations)
    public ErrorResponse() {
    }
}
