package sn.zahra.thiaw.gestiondesfichiers.Web.Controllers;

import org.springframework.http.ResponseEntity;
import sn.zahra.thiaw.gestiondesfichiers.Filters.ApiResponse;

import java.util.List;

public interface BaseController<E, ID, R> {

    ResponseEntity<ApiResponse<R>> create(E entity);

    ResponseEntity<ApiResponse<R>> update(ID id, E entity);

    ResponseEntity<ApiResponse<R>> getById(ID id);

    ResponseEntity<ApiResponse<List<R>>> getAll();

    ResponseEntity<ApiResponse<Void>> delete(ID id);
}
