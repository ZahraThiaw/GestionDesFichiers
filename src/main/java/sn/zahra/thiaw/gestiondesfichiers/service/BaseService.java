package sn.zahra.thiaw.gestiondesfichiers.service;

import java.util.List;

public interface BaseService<E, ID> {

    E create(E entity);

    E update(ID id, E entity);

    E getById(ID id);

    List<E> getAll();

    void delete(ID id);
}

